/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.remotefetch.core.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchCoreConfiguration;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.RemoteFetchConstants;

import java.io.File;
import java.util.UUID;

/**
 * Parser for core configuration from deployment.toml file.
 */
public class RemoteFetchConfigurationUtils {

    private static final Log log = LogFactory.getLog(RemoteFetchConfigurationUtils.class);


    /**
     * Parse configuration from deployment toml file.
     *
     * @return RemoteFetchCoreConfiguration
     * @throws RemoteFetchCoreException
     */
    public static RemoteFetchCoreConfiguration parseConfiguration() throws RemoteFetchCoreException {

        boolean isEnabled = false;
        File workingDirectory = null;

        String isEnabledProperty = IdentityUtil.getProperty("RemoteFetch.FetchEnabled");
        String workingDirectoryProperty = IdentityUtil.getProperty("RemoteFetch.WorkingDirectory");

        if (isEnabledProperty != null && !isEnabledProperty.isEmpty()) {
            isEnabled = isEnabledProperty.equalsIgnoreCase("true");
        }

        if (workingDirectoryProperty != null && !workingDirectoryProperty.isEmpty()) {
            workingDirectory = new File(workingDirectoryProperty);
            validateDirectory(workingDirectory);
        }

        return new RemoteFetchCoreConfiguration(workingDirectory, isEnabled);

    }

    private static void validateDirectory(File workingDirectory) throws RemoteFetchCoreException {

        if (!workingDirectory.isDirectory()) {
            throw new RemoteFetchCoreException("Not a valid WorkingDirectory for RemoteFetchCore");
        }
    }

    /**
     * Generate UUID.
     *
     * @return UUID
     */
    public static String generateUniqueID() {

        return UUID.randomUUID().toString();
    }

    /**
     * Parse the Default Items per Page needed to display.
     *
     * @return defaultItemsPerPage
     */
    public static int parseDefaultItemsPerPage() {

        int defaultItemsPerPage = RemoteFetchConstants.DEFAULT_ITEMS_PRE_PAGE;
        try {
            String defaultItemsPerPageProperty = IdentityUtil.getProperty(RemoteFetchConstants
                    .DEFAULT_ITEMS_PRE_PAGE_PROPERTY);
            if (StringUtils.isNotBlank(defaultItemsPerPageProperty)) {
                int defaultItemsPerPageConfig = Integer.parseInt(defaultItemsPerPageProperty);
                if (defaultItemsPerPageConfig > 0) {
                    defaultItemsPerPage = defaultItemsPerPageConfig;
                }
            }
        } catch (NumberFormatException e) {
            log.warn("Error occurred while parsing the 'DefaultItemsPerPage' property value in identity.xml.", e);
        }
        return defaultItemsPerPage;
    }

    /**
     * Get the Maximum Items per Page needed to display.
     * @return maximumItemsPerPage
     */
    public static int parseMaximumItemPerPage() {

        int maximumItemsPerPage = RemoteFetchConstants.DEFAULT_MAXIMUM_ITEMS_PRE_PAGE;
        String maximumItemsPerPagePropertyValue =
                IdentityUtil.getProperty(RemoteFetchConstants.MAXIMUM_ITEMS_PRE_PAGE_PROPERTY);
        if (StringUtils.isNotBlank(maximumItemsPerPagePropertyValue)) {
            try {
                maximumItemsPerPage = Integer.parseInt(maximumItemsPerPagePropertyValue);
            } catch (NumberFormatException e) {
                log.warn("Error occurred while parsing the 'MaximumItemsPerPage' property value in identity.xml.", e);
            }
        }
        return maximumItemsPerPage;
    }
}
