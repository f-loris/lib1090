package de.serosystems.lib1090.msgs.bds;

import de.serosystems.lib1090.exceptions.BadFormatException;
import de.serosystems.lib1090.msgs.adsb.TCASResolutionAdvisoryMsg;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ACASActiveResolutionAdvisoryReportTest {

    private static byte[] msg;
    private static ACASActiveResolutionAdvisoryReport acasReport;

    @BeforeClass
    public static void setup() throws BadFormatException {

        msg = new byte[]{
                (byte) 0b00110000, (byte) 0b00000000, (byte) 0b00000011, (byte) 0b11111100, (byte) 0b00000000,
                (byte) 0b00000000, (byte) 0b00000000
        };

        acasReport = new ACASActiveResolutionAdvisoryReport(msg);


    }

    @Test
    public void bdsCode() {
        Assert.assertEquals(BDSRegister.bdsCode.ACAS_ACTIVE_RESOLUTION_ADVISORY, acasReport.getBds());
    }

    @Test
    public void activeResolutionAdvisories() {

        boolean[] activeResolutionAdvisories = acasReport.getActiveResolutionAdvisories();
        Assert.assertFalse(activeResolutionAdvisories[0]);
        Assert.assertFalse(activeResolutionAdvisories[1]);
        Assert.assertFalse(activeResolutionAdvisories[2]);
        Assert.assertFalse(activeResolutionAdvisories[3]);
        Assert.assertFalse(activeResolutionAdvisories[4]);
        Assert.assertFalse(activeResolutionAdvisories[5]);
        Assert.assertFalse(activeResolutionAdvisories[6]);
        Assert.assertFalse(activeResolutionAdvisories[7]);
        Assert.assertFalse(activeResolutionAdvisories[8]);
        Assert.assertFalse(activeResolutionAdvisories[9]);
        Assert.assertFalse(activeResolutionAdvisories[10]);
        Assert.assertFalse(activeResolutionAdvisories[11]);
        Assert.assertFalse(activeResolutionAdvisories[12]);
        Assert.assertFalse(activeResolutionAdvisories[13]);

    }

    @Test
    public void resolutionAdvisoriesComponentsRecord() {

        boolean[] resolutionAdvisoriesComponentsRecord = acasReport.getResolutionAdvisoriesComplementsRecord();
        Assert.assertTrue(resolutionAdvisoriesComponentsRecord[0]);
        Assert.assertTrue(resolutionAdvisoriesComponentsRecord[1]);
        Assert.assertTrue(resolutionAdvisoriesComponentsRecord[2]);
        Assert.assertTrue(resolutionAdvisoriesComponentsRecord[3]);

    }

    @Test
    public void resolutionAdvisoryTerminated() {
        Assert.assertTrue(acasReport.hasRATerminated());
    }

    @Test
    public void multipleThreatEncounter() {
        Assert.assertTrue(acasReport.hasMultiThreatEncounter());
    }

    @Test
    public void threatTypeIndicator() {
        Assert.assertEquals(3, (int) acasReport.getThreatType());
    }

    @Test
    public void threatIdentityData() throws BadFormatException {

        ThreatIdentityData threatIdentityData0 = TCASResolutionAdvisoryMsg.extractThreatIdentityData((short) 0, msg);
        Assert.assertNull(threatIdentityData0);

        ThreatIdentityData threatIdentityData1 = TCASResolutionAdvisoryMsg.extractThreatIdentityData((short) 1, msg);
        Assert.assertNotNull(threatIdentityData1);
        Assert.assertEquals(0L, threatIdentityData1.getIcao24().longValue());
        Assert.assertNull(threatIdentityData1.getAltitudeCode());
        Assert.assertNull(threatIdentityData1.getEncodedRange());
        Assert.assertNull(threatIdentityData1.getEncodedBearing());

        ThreatIdentityData threatIdentityData2 = TCASResolutionAdvisoryMsg.extractThreatIdentityData((short) 2, msg);
        Assert.assertNotNull(threatIdentityData2);
        Assert.assertNull(threatIdentityData2.getIcao24());
        Assert.assertEquals(0, threatIdentityData2.getAltitudeCode().shortValue());
        Assert.assertEquals(0, threatIdentityData2.getEncodedRange().shortValue());
        Assert.assertEquals(0, threatIdentityData2.getEncodedBearing().shortValue());

        ThreatIdentityData threatIdentityData3 = TCASResolutionAdvisoryMsg.extractThreatIdentityData((short) 3, msg);
        Assert.assertNull(threatIdentityData3);

    }

    @Test
    public void threatIdentityDataWithIcao() throws BadFormatException {
        // icao24 set here is 0xabcdef = 0b 10101011 11001101 11101111
        byte[] msg = new byte[] {
                0b00110000, 0b01000000, 0b01000000, 0b01110110, (byte) 0b10101111, 0b00110111, (byte) 0b10111100
        };

        final ACASActiveResolutionAdvisoryReport acasReport = new ACASActiveResolutionAdvisoryReport(msg);

        Assert.assertTrue(acasReport.hasRATerminated());
        Assert.assertTrue(acasReport.hasMultiThreatEncounter());

        Assert.assertEquals(0b01000000010000, acasReport.getActiveRA());
        Assert.assertArrayEquals(new boolean[] {
                false,
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                false,
        }, acasReport.getActiveResolutionAdvisories());

        Assert.assertEquals(1, acasReport.getRACRecord());
        Assert.assertArrayEquals(new boolean[] { false, false, false, true }, acasReport.getResolutionAdvisoriesComplementsRecord());

        Assert.assertEquals(0b10101011110011011110111100, acasReport.getThreatIdentity().intValue());
        Assert.assertEquals(0xabcdef, acasReport.getThreatIdentityData().getIcao24().intValue());
    }

}
