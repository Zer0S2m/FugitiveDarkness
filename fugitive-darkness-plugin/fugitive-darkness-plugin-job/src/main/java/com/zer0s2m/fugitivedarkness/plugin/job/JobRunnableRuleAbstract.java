package com.zer0s2m.fugitivedarkness.plugin.job;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class JobRunnableRuleAbstract implements JobRunnableRule {

    protected final Set<Rule> rules = new HashSet<>();

    protected Map<String, Object> properties;

    /**
     * Add a rule to start a deferred task.
     *
     * @param rule Rule.
     */
    @Override
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    /**
     * Clear all rules for running a deferred task.
     */
    @Override
    public void clearRule() {
        rules.clear();
    }

    /**
     * Run all validation rules before starting a deferred task.
     *
     * @param type The type of deferred task to run certain rules.
     * @throws JobException               A general exception.
     * @throws JobValidationRuleException Validation was unsuccessful.
     */
    @Override
    public void run(Object type) throws JobException {
        Set<Rule> filteredRules = new HashSet<>();

        rules.forEach(rule -> {
            if (rule.getTypeTask().equals(type)) {
                filteredRules.add(rule);
            }
        });

        for (Rule rule : filteredRules) {
            rule.setTypeTask(type);
            rule.validation(properties);
        }
    }

    abstract protected void setProperties(Map<String, Object> properties);

}
