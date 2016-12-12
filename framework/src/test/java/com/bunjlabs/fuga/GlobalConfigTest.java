/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class GlobalConfigTest {

    private FugaApp prepareTestApp() {
        FugaApp fa = new FugaApp() {

            @Override
            public void prepare() {
            }
        };
        return fa;
    }

    @Test
    public void checkFugaVersion() {
        FugaApp app = prepareTestApp();

        String version = app.getConfiguration().get("fuga.version");

        Assert.assertNotNull(version);
        Assert.assertTrue(version.length() > 0);
    }
}
