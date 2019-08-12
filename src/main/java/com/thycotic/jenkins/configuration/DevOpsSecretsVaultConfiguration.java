package com.thycotic.jenkins.configuration;

import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;
import com.thycotic.jenkins.credentials.ThycoticVaultCredentials;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import java.io.Serializable;
import java.util.List;

public class DevOpsSecretsVaultConfiguration extends AbstractDescribableImpl<DevOpsSecretsVaultConfiguration> implements Serializable {

    private String thycoticCredentialId;

    public DevOpsSecretsVaultConfiguration() {
    }

    /**
     * Constructor bound to config.jelly to create a new Configuration based on the auth credential to the vault
     */
    @DataBoundConstructor
    public DevOpsSecretsVaultConfiguration(String thycoticCredentialId) {
        this.thycoticCredentialId = thycoticCredentialId;
    }

    public String getThycoticCredentialId() {
        return thycoticCredentialId;
    }

    @DataBoundSetter
    public void setThycoticCredentialId(String thycoticCredentialId) {
        this.thycoticCredentialId = thycoticCredentialId;
    }

    /**
     * Create a new config and pull the parent auth credential if the current config does not have
     * the auth credential set
     *
     * @param parent parent DevOpsSecretsVaultConfiguration
     * @return new DevOpsSecretsVaultConfiguration
     */
    public DevOpsSecretsVaultConfiguration mergeWithParent(DevOpsSecretsVaultConfiguration parent) {
        if (parent == null) {
            return this;
        }
        DevOpsSecretsVaultConfiguration result = new DevOpsSecretsVaultConfiguration(this.getThycoticCredentialId());
        if (StringUtils.isBlank(result.getThycoticCredentialId())) {
            result.setThycoticCredentialId(parent.getThycoticCredentialId());
        }
        return result;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<DevOpsSecretsVaultConfiguration> {
        @Override
        public String getDisplayName() {
            return "Thycotic DevOps Secrets Vault Configuration";
        }

        /**
         * Used to populate dropdown in config.jelly for choosing credential to auth to the tenants vault.
         */
        public ListBoxModel doFillThycoticCredentialIdItems(@AncestorInPath Item item, @QueryParameter String uri) {
            List<DomainRequirement> domainRequirements = URIRequirementBuilder.fromUri(uri).build();
            return new StandardListBoxModel().includeEmptyValue().includeAs(ACL.SYSTEM, item,
                    ThycoticVaultCredentials.class, domainRequirements);
        }
    }
}