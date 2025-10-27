package com.tvt.common;

import java.util.Map;

public class CommonParseValue {

	public CommonParseValue() {
	}

	public double getDouble(Map<String, String> map, String key, double def) {
		String v = map.get(key);
		if (v == null) {
			return def;
		}
		v = v.replaceAll("[^0-9.\\-eE+]", "");

		try {
			return Double.parseDouble(v);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}

	}

	public double parseDistance(String s) {
		if (s == null) {
			return 1.0;
		}

		return getDouble(Map.of("dist", s.replace("m", "").trim()), "dist", 1.0);
	}

	public double parseTemp(String s) {
		if (s == null) {
			return 20.0;
		}

		return getDouble(Map.of("temp", s), "temp", 20.0);
	}

	public FlirMetadataParam parseMetadata(Map<String, String> map) {
		FlirMetadataParam m = new FlirMetadataParam();
		
		// planck parameter
		m.setPlanckR1(getDouble(map, "Planck R1"));
		m.setPlanckB(getDouble(map, "Planck B"));
		m.setPlanckF(getDouble(map, "Planck F"));
		m.setPlanckO(getDouble(map, "Planck O"));
		m.setPlanckR2(getDouble(map, "Planck R2"));
		
		// Atmospheric parameter
		m.setEmissivity(getDouble(map, "Emissivity", m.getEmissivity()));
		m.setObjectDistance(parseDistance(map.get("Object Distance")));
		m.setAtmosphericTemperature(parseTemp(map.get("Atmospheric Temperature")));
		m.setReflectedApparentTemperature(parseTemp(map.get("Reflected Apparent Temperature")));
		m.setRelativeHumidity(getDouble(map, "Relative Humidity", m.getRelativeHumidity()));
		m.setIrWindowTemperature(parseTemp(map.get("IR Window Temperature")));
		m.setIrWindowTransmission(getDouble(map, "IR Window Transmission", m.getIrWindowTransmission()));

		m.setAtmosphericTransAlpha1(getDouble(map, "Atmospheric Trans Alpha 1", 0.0));
		m.setAtmosphericTransAlpha2(getDouble(map, "Atmospheric Trans Alpha 2", 0.0));
		m.setAtmosphericTransBeta1(getDouble(map, "Atmospheric Trans Beta 1", 0.0));
		m.setAtmosphericTransBeta2(getDouble(map, "Atmospheric Trans Beta 2", 0.0));
		m.setAtmosphericTransX(getDouble(map, "Atmospheric Trans X", 1.0));
		
		m.setCameraTemperatureRangeMin(parseTemp(map.get("Camera Temperature Range Min")));
		m.setCameraTemperatureRangeMax(parseTemp(map.get("Camera Temperature Range Max")));
		m.setRawValueRangeMin(getDouble(map, "Raw Value Range Min", 0));
		m.setRawValueRangeMax(getDouble(map, "Raw Value Range Max", 65535));

		// Atmospheric transmission (simplified FLIR model)
		double sqrtD = Math.sqrt(m.getObjectDistance());
		double tauAtm = 1.0 - m.getAtmosphericTransAlpha1() * Math.exp(-m.getAtmosphericTransBeta1() * sqrtD)
				- m.getAtmosphericTransAlpha2() * Math.exp(-m.getAtmosphericTransBeta2() * sqrtD);
		m.setTau(tauAtm * m.getIrWindowTransmission());

		// Effective R
		double atmFactor = (1.0 - m.getTau()) / m.getTau();
		m.setR(m.getPlanckR1() / (m.getEmissivity() * m.getTau()) + m.getPlanckR2() * atmFactor);
		
		// vision image parameter
		m.setReal2IR(getDouble(map, "Real 2 IR"));
		m.setOffsetX(getDouble(map, "Offset X"));
		m.setOffsetY(getDouble(map, "Offset Y"));
		m.setPipX1(getDouble(map, "PiP X1"));
		m.setPipX2(getDouble(map, "PiP X2"));
		m.setPipY1(getDouble(map, "PiP Y1"));
		m.setPipY2(getDouble(map, "PiP Y2"));
		m.setRawThermalImageWidth(getDouble(map, "Raw Thermal Image Width"));
		m.setRawThermalImageHeight(getDouble(map, "Raw Thermal Image Height"));
		
		return m;
	}

	private double getDouble(Map<String, String> map, String key) {
		return getDouble(map, key, Double.NaN);
	}

}
