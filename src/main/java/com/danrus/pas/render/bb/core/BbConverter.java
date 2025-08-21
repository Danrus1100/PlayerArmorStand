package com.danrus.pas.render.bb.core;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BbConverter {

    public static LayerDefinition convertToLayerDefinition(BbModelContainer model) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        // Create a map of UUID to element for quick lookup
        Map<String, BbElement> elementMap = new HashMap<>();
        for (BbElement element : model.getElements()) {
            elementMap.put(element.uuid, element);
        }

        // Process outliner structure
        for (BbOutliner outliner : model.getOutliner()) {
            processOutliner(outliner, root, elementMap);
        }

        // Get texture resolution
        int textureWidth = 64;
        int textureHeight = 64;
        if (model.getResolution() != null) {
            textureWidth = model.getResolution().getWidth();
            textureHeight = model.getResolution().getHeight();
        }

        return LayerDefinition.create(meshDefinition, textureWidth, textureHeight);
    }

    private static void processOutliner(BbOutliner outliner, PartDefinition parent, Map<String, BbElement> elementMap) {
        PartDefinition partDefinition = parent;

        if (outliner.name != null && !outliner.name.isEmpty() && outliner.children != null && !outliner.children.isEmpty()) {
            CubeListBuilder cubeBuilder = CubeListBuilder.create();
            boolean hasCubes = false;

            for (Object child : outliner.children) {
                if (child instanceof String uuid) {
                    BbElement element = elementMap.get(uuid);
                    if (element != null && "cube".equals(element.type)) {
                        addCubeToBuilder(cubeBuilder, element);
                        hasCubes = true;
                    }
                } else if (child instanceof BbOutliner childOutliner) {
                    processOutliner(childOutliner, partDefinition, elementMap);
                } else if (child instanceof Map) {
                    // Convert Map -> BbOutliner
                    BbOutliner childOutliner = BbOutliner.fromMap((Map<String, Object>) child);
                    processOutliner(childOutliner, partDefinition, elementMap);
                }
            }

            PartPose pose = PartPose.ZERO;
            if (outliner.origin != null && outliner.origin.size() >= 3) {
                pose = PartPose.offset(
                        outliner.origin.get(0).floatValue(),
                        outliner.origin.get(1).floatValue(),
                        outliner.origin.get(2).floatValue()
                );
            }

            if (hasCubes) {
                partDefinition = parent.addOrReplaceChild(outliner.name, cubeBuilder, pose);
            }
        }

        // If no cubes were added, we still need to create a part for this outliner
        if (outliner.children != null) {
            for (Object child : outliner.children) {
                if (child instanceof BbOutliner childOutliner) {
                    processOutliner(childOutliner, partDefinition, elementMap);
                }
            }
        }
    }

    private static void addCubeToBuilder(CubeListBuilder builder, BbElement element) {
        if (element.from == null || element.to == null || element.from.size() < 3 || element.to.size() < 3) {
            return;
        }

        // Convert Blockbench coordinates to Minecraft coordinates
        float fromX = element.from.get(0).floatValue();
        float fromY = element.from.get(1).floatValue();
        float fromZ = element.from.get(2).floatValue();

        float toX = element.to.get(0).floatValue();
        float toY = element.to.get(1).floatValue();
        float toZ = element.to.get(2).floatValue();

        // Calculate dimensions
        float width = Math.abs(toX - fromX);
        float height = Math.abs(toY - fromY);
        float depth = Math.abs(toZ - fromZ);

        // Use minimum coordinates as position
        float x = Math.min(fromX, toX);
        float y = Math.min(fromY, toY);
        float z = Math.min(fromZ, toZ);

        // Get texture coordinates (simplified - uses north face UV)
        int texU = 0, texV = 0;
        if (element.faces != null && element.faces.containsKey("north")) {
            BbFace northFace = element.faces.get("north");
            if (northFace.uv != null && northFace.uv.size() >= 2) {
                texU = northFace.uv.get(0).intValue();
                texV = northFace.uv.get(1).intValue();
            }
        }

        builder.texOffs(texU, texV).addBox(x, y, z, width, height, depth);
    }
}