package com.tairanchina.csp.avc.utils;

import com.tairanchina.csp.avm.utils.VersionCompareUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2019/2/25.
 *
 * @author 迹_Jason
 */
public class VersionCompareUtilsTest {

    @Test
    public void test001() {
        String version1 = "1.0.0";
        String version2 = "1.0.1";
        int diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "1.0.0";
        version2 = "1.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "1.0.0.0";
        version2 = "1.0.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "9.0.0";
        version2 = "10.0.0";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "9.0.0.0";
        version2 = "10.0.0.0";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "9.0.0-beta";
        version2 = "10.0.0-beta";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "9.0.0.0-beta";
        version2 = "10.0.0.0-beta";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "10.0.0-beta";
        version2 = "10.0.0-alpha";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "9.0.0.0-beta";
        version2 = "9.0.0.0-beta";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "9.0.0-beta";
        version2 = "9.0.0-rc";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue( diff > 0);

        version1 = "10.0.0.0-beta";
        version2 = "10.0.0.0-alpha";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "10.0.0.0";
        version2 = "10.0.0.0-alpha";
        diff = VersionCompareUtils.compareVersion(version1, version2);

        Assert.assertTrue(diff > 0);

        version1 = "1.0.0";
        version2 = "10.0.0";
        diff = VersionCompareUtils.compareVersion(version1, version2);

        Assert.assertTrue(diff > 0);

        version1 = "1.0.0";
        version2 = "10.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "1.10.0";
        version2 = "1.0.11";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "11.0.0";
        version2 = "1.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "1.0.0";
        version2 = "1.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "1.0.0";
        version2 = "1.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "1.0.0.0";
        version2 = "1.0.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "9.0.0";
        version2 = "10.0.0";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "9.0.0.0";
        version2 = "10.0.0.0";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "9.0.0-beta";
        version2 = "10.0.0-beta";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "9.0.0.0-beta";
        version2 = "10.0.0.0-beta";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "10.0.0-beta";
        version2 = "10.0.0-alpha";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "9.0.0.0-beta";
        version2 = "9.0.0.0-beta";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "9.0.0-beta";
        version2 = "9.0.0-rc";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "10.0.0.0-beta";
        version2 = "10.0.0.0-alpha";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "10.0.0.0";
        version2 = "10.0.0.0-alpha";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "1.0.0";
        version2 = "10.0.0";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "1.0.0";
        version2 = "10.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "1.10.0";
        version2 = "1.0.11";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "11.0.0";
        version2 = "1.0.1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

        version1 = "1.0.0.a";
        version2 = "1.0.0.a";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "100";
        version2 = "1.0.0.a";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "100";
        version2 = "12q1";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);


        version1 = "100";
        version2 = "99";
        diff = VersionCompareUtils.compareVersion(version1, version2);
        Assert.assertTrue(diff > 0);

    }
}
