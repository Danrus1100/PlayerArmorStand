package com.danrus.pas.api;

import java.util.ArrayList;
import java.util.List;

public class PasFeature {
    private String literal;

    private int mandatoryArguments = 0;
    private final List<String> mandatoryArgumentsList = new ArrayList<>();

    private int optionalArguments = 0;
    private final List<String> optionalArgumentsList = new ArrayList<>();



    private PasFeature() {}

    public static Builder builder(String literal) {
        return new Builder(literal);
    }

    public static class Builder {
        private final PasFeature feature;

        public Builder(String literal) {
            this.feature = new PasFeature();
            this.feature.literal = literal;
        }

        public Builder addMandatoryArgument(String argument) {
            this.feature.mandatoryArgumentsList.add(argument);
            return this;
        }

        public Builder addOptionalArgument(String argument) {
            this.feature.mandatoryArgumentsList.add(argument);
            return this;
        }

        public PasFeature build() {
            return this.feature;
        }
    }

//    @Override
//    public String toString() {
//        return "PasFeature(" + literal
//                + (mandatoryArguments.isEmpty() ? "" : ", " + String.join(", ", mandatoryArguments))
//                + (optionalArguments.isEmpty() ? "" : ", [" + String.join(", ", optionalArguments) + "]")
//                + ")";
//    }
}