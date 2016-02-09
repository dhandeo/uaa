/*******************************************************************************
 * Cloud Foundry
 * Copyright (c) [2009-2015] Pivotal Software, Inc. All Rights Reserved.
 * <p>
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 * <p>
 * This product includes a number of subcomponents with
 * separate copyright notices and license terms. Your use of these
 * subcomponents is subject to the terms and conditions of the
 * subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/
package org.cloudfoundry.identity.uaa.impl.config;

import org.cloudfoundry.identity.uaa.zone.IdentityZone;
import org.cloudfoundry.identity.uaa.zone.IdentityZoneConfiguration;
import org.cloudfoundry.identity.uaa.zone.IdentityZoneProvisioning;
import org.cloudfoundry.identity.uaa.zone.TokenPolicy;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

public class IdentityZoneConfigurationBootstrap implements InitializingBean {

    private TokenPolicy tokenPolicy;
    private IdentityZoneProvisioning provisioning;
    private boolean selfServiceLinksEnabled = true;
    private String homeRedirect = null;
    private Map<String,String> selfServiceLinks;


    public IdentityZoneConfigurationBootstrap(IdentityZoneProvisioning provisioning) {
        this.provisioning = provisioning;
    }

    @Override
    public void afterPropertiesSet() {
        IdentityZone identityZone = provisioning.retrieve(IdentityZone.getUaa().getId());
        IdentityZoneConfiguration definition = new IdentityZoneConfiguration(tokenPolicy);
        definition.getLinks().getService().setSelfServiceLinksEnabled(selfServiceLinksEnabled);
        definition.getLinks().setHomeRedirect(homeRedirect);
        if (selfServiceLinks!=null) {
            String signup = selfServiceLinks.get("signup");
            String passwd = selfServiceLinks.get("passwd");
            if (hasText(signup)) {
                definition.getLinks().getService().setSignup(signup);
            }
            if (hasText(passwd)) {
                definition.getLinks().getService().setPasswd(passwd);
            }
        }
        identityZone.setConfig(definition);
        provisioning.update(identityZone);
    }



    public void setTokenPolicy(TokenPolicy tokenPolicy) {
        this.tokenPolicy = tokenPolicy;
    }

    public void setSelfServiceLinksEnabled(boolean selfServiceLinksEnabled) {
        this.selfServiceLinksEnabled = selfServiceLinksEnabled;
    }

    public void setHomeRedirect(String homeRedirect) {
        if ("null".equals(homeRedirect)) {
            this.homeRedirect = null;
        } else {
            this.homeRedirect = homeRedirect;
        }
    }

    public void setSelfServiceLinks(Map<String, String> links) {
        this.selfServiceLinks = links;
    }
}
