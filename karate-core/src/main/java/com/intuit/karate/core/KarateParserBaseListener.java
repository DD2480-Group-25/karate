package com.intuit.karate.core;public abstract class KarateParserBaseListener {
    public abstract void enterFeatureHeader(KarateParser.FeatureHeaderContext ctx);

    public abstract void enterBackground(KarateParser.BackgroundContext ctx);

    public abstract void enterScenario(KarateParser.ScenarioContext ctx);

    public abstract void enterScenarioOutline(KarateParser.ScenarioOutlineContext ctx);
}
