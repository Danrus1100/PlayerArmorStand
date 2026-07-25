package com.danrus.pas.api.info;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public final class NameInfoPattern implements NameInfoLike, Predicate<NameInfo> {

    private final Predicate<NameInfo> infoMatcher;
    private final Predicate<String> baseMatcher;
    private final @Nullable String baseName;
    private final Map<Class<? extends RenameFeature>, Predicate<RenameFeature>> featureMatchers;

    public NameInfoPattern() {
        this(info -> true, base -> true, null, Map.of());
    }

    private NameInfoPattern(
            Predicate<NameInfo> infoMatcher,
            Predicate<String> baseMatcher,
            @Nullable String baseName,
            Map<Class<? extends RenameFeature>, Predicate<RenameFeature>> featureMatchers
    ) {
        this.infoMatcher = infoMatcher;
        this.baseMatcher = baseMatcher;
        this.baseName = baseName;
        this.featureMatchers = Map.copyOf(featureMatchers);
    }

    public static NameInfoPattern any() {
        return new NameInfoPattern();
    }

    public static NameInfoPattern exact(@NotNull NameInfo info) {
        Objects.requireNonNull(info, "info");
        return new NameInfoPattern(info::equals, base -> true, info.base(), Map.of());
    }

    public NameInfoPattern withBaseName(@NotNull String baseName) {
        Objects.requireNonNull(baseName, "baseName");
        String knownBaseName = this.baseName == null || this.baseName.equals(baseName)
                ? baseName
                : null;
        return new NameInfoPattern(
                infoMatcher,
                baseMatcher.and(baseName::equals),
                knownBaseName,
                featureMatchers
        );
    }

    public NameInfoPattern withBaseName(@NotNull Predicate<String> matcher) {
        Objects.requireNonNull(matcher, "matcher");
        return new NameInfoPattern(infoMatcher, baseMatcher.and(matcher), baseName, featureMatchers);
    }

    public NameInfoPattern withBase(@NotNull String base) {
        return withBaseName(base);
    }

    public NameInfoPattern withBase(@NotNull Predicate<String> matcher) {
        return withBaseName(matcher);
    }

    public <T extends RenameFeature> NameInfoPattern withFeature(@NotNull Class<T> featureClass) {
        return withFeatureState(featureClass, feature -> true);
    }

    public <T extends RenameFeature> NameInfoPattern withFeature(
            @NotNull Class<T> featureClass,
            @NotNull T expected
    ) {
        Objects.requireNonNull(expected, "expected");
        return withFeatureState(featureClass, expected::equals);
    }

    public <T extends RenameFeature> NameInfoPattern withFeatureState(
            @NotNull Class<T> featureClass,
            @NotNull Predicate<? super T> matcher
    ) {
        Objects.requireNonNull(featureClass, "featureClass");
        Objects.requireNonNull(matcher, "matcher");

        return withRawFeatureMatcher(featureClass, feature -> {
            if (!featureClass.isInstance(feature)) {
                return false;
            }
            return matcher.test(featureClass.cast(feature));
        });
    }

    public <T extends RenameFeature> NameInfoPattern withActiveFeature(@NotNull Class<T> featureClass) {
        return withFeatureState(featureClass, RenameFeature::isActive);
    }

    public <T extends RenameFeature> NameInfoPattern withInactiveFeature(@NotNull Class<T> featureClass) {
        return withFeatureState(featureClass, feature -> !feature.isActive());
    }

    public NameInfoPattern withoutFeature(@NotNull Class<? extends RenameFeature> featureClass) {
        Objects.requireNonNull(featureClass, "featureClass");
        return withRawFeatureMatcher(featureClass, Objects::isNull);
    }

    private NameInfoPattern withRawFeatureMatcher(
            Class<? extends RenameFeature> featureClass,
            Predicate<RenameFeature> matcher
    ) {
        LinkedHashMap<Class<? extends RenameFeature>, Predicate<RenameFeature>> matchers =
                new LinkedHashMap<>(featureMatchers);
        matchers.merge(featureClass, matcher, Predicate::and);
        return new NameInfoPattern(infoMatcher, baseMatcher, baseName, matchers);
    }

    public boolean matches(@NotNull NameInfo info) {
        Objects.requireNonNull(info, "info");
        if (!infoMatcher.test(info) || !baseMatcher.test(info.base())) {
            return false;
        }

        for (Map.Entry<Class<? extends RenameFeature>, Predicate<RenameFeature>> entry
                : featureMatchers.entrySet()) {
            RenameFeature feature = info.features().get(entry.getKey());
            if (!entry.getValue().test(feature)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean test(@NotNull NameInfo info) {
        return matches(info);
    }

    @Override
    public @NotNull NameInfoPattern toPattern() {
        return this;
    }

    public @NotNull Optional<String> getBaseName() {
        return Optional.ofNullable(baseName);
    }

    public boolean canBaseNameDisplayed() {
        return baseName != null;
    }

    @Override
    public String toString() {
        return baseName != null ? baseName : super.toString();
    }
}
