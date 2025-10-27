package com.tvt.common;

public class FlirMetadataParam {
	private double planckR1 = 16788.896, planckB = 1432.2, planckF = 1.0, planckO = -4159, planckR2 = 0.011489335;
	private double emissivity = 0.85;
	private double objectDistance = 1.0;
	private double atmosphericTemperature = 23.0, reflectedApparentTemperature = 20.0, relativeHumidity = 50.0;
	private double irWindowTemperature = 20.0, irWindowTransmission = 1.0;
	private double atmosphericTransAlpha1, atmosphericTransAlpha2, atmosphericTransBeta1 = 0.003180,
			atmosphericTransBeta2 = 0.003180, atmosphericTransX = 0.732000;
	private double tau, R;

	private double cameraTemperatureRangeMin = -20.0;
	private double cameraTemperatureRangeMax = 120.0;
	private double rawValueRangeMin = 5926.0;
	private double rawValueRangeMax = 55419.0;

	private double real2IR = 1.3206022977829;
	private double offsetX = 0.0;
	private double offsetY = 0.0;
	private double pipX1 = 0.0;
	private double pipX2 = 1024.0;
	private double pipY1 = 0;
	private double pipY2 = 768;
	private double rawThermalImageWidth = 464.0;
	private double rawThermalImageHeight = 348.0;

	public FlirMetadataParam() {
	}

	public double getPlanckR1() {
		return planckR1;
	}

	public void setPlanckR1(double planckR1) {
		this.planckR1 = planckR1;
	}

	public double getPlanckB() {
		return planckB;
	}

	public void setPlanckB(double planckB) {
		this.planckB = planckB;
	}

	public double getPlanckF() {
		return planckF;
	}

	public void setPlanckF(double planckF) {
		this.planckF = planckF;
	}

	public double getPlanckO() {
		return planckO;
	}

	public void setPlanckO(double planckO) {
		this.planckO = planckO;
	}

	public double getPlanckR2() {
		return planckR2;
	}

	public void setPlanckR2(double planckR2) {
		this.planckR2 = planckR2;
	}

	public double getEmissivity() {
		return emissivity;
	}

	public void setEmissivity(double emissivity) {
		this.emissivity = emissivity;
	}

	public double getObjectDistance() {
		return objectDistance;
	}

	public void setObjectDistance(double objectDistance) {
		this.objectDistance = objectDistance;
	}

	public double getAtmosphericTemperature() {
		return atmosphericTemperature;
	}

	public void setAtmosphericTemperature(double atmosphericTemperature) {
		this.atmosphericTemperature = atmosphericTemperature;
	}

	public double getReflectedApparentTemperature() {
		return reflectedApparentTemperature;
	}

	public void setReflectedApparentTemperature(double reflectedApparentTemperature) {
		this.reflectedApparentTemperature = reflectedApparentTemperature;
	}

	public double getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public double getIrWindowTemperature() {
		return irWindowTemperature;
	}

	public void setIrWindowTemperature(double irWindowTemperature) {
		this.irWindowTemperature = irWindowTemperature;
	}

	public double getIrWindowTransmission() {
		return irWindowTransmission;
	}

	public void setIrWindowTransmission(double irWindowTransmission) {
		this.irWindowTransmission = irWindowTransmission;
	}

	public double getAtmosphericTransAlpha1() {
		return atmosphericTransAlpha1;
	}

	public void setAtmosphericTransAlpha1(double atmosphericTransAlpha1) {
		this.atmosphericTransAlpha1 = atmosphericTransAlpha1;
	}

	public double getAtmosphericTransAlpha2() {
		return atmosphericTransAlpha2;
	}

	public void setAtmosphericTransAlpha2(double atmosphericTransAlpha2) {
		this.atmosphericTransAlpha2 = atmosphericTransAlpha2;
	}

	public double getAtmosphericTransBeta1() {
		return atmosphericTransBeta1;
	}

	public void setAtmosphericTransBeta1(double atmosphericTransBeta1) {
		this.atmosphericTransBeta1 = atmosphericTransBeta1;
	}

	public double getAtmosphericTransBeta2() {
		return atmosphericTransBeta2;
	}

	public void setAtmosphericTransBeta2(double atmosphericTransBeta2) {
		this.atmosphericTransBeta2 = atmosphericTransBeta2;
	}

	public double getAtmosphericTransX() {
		return atmosphericTransX;
	}

	public void setAtmosphericTransX(double atmosphericTransX) {
		this.atmosphericTransX = atmosphericTransX;
	}

	public double getTau() {
		return tau;
	}

	public void setTau(double tau) {
		this.tau = tau;
	}

	public double getR() {
		return R;
	}

	public void setR(double r) {
		R = r;
	}

	public double getCameraTemperatureRangeMin() {
		return cameraTemperatureRangeMin;
	}

	public void setCameraTemperatureRangeMin(double cameraTemperatureRangeMin) {
		this.cameraTemperatureRangeMin = cameraTemperatureRangeMin;
	}

	public double getCameraTemperatureRangeMax() {
		return cameraTemperatureRangeMax;
	}

	public void setCameraTemperatureRangeMax(double cameraTemperatureRangeMax) {
		this.cameraTemperatureRangeMax = cameraTemperatureRangeMax;
	}

	public double getRawValueRangeMin() {
		return rawValueRangeMin;
	}

	public void setRawValueRangeMin(double rawValueRangeMin) {
		this.rawValueRangeMin = rawValueRangeMin;
	}

	public double getRawValueRangeMax() {
		return rawValueRangeMax;
	}

	public void setRawValueRangeMax(double rawValueRangeMax) {
		this.rawValueRangeMax = rawValueRangeMax;
	}

	public double getReal2IR() {
		return real2IR;
	}

	public void setReal2IR(double real2ir) {
		real2IR = real2ir;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}

	public double getPipX1() {
		return pipX1;
	}

	public void setPipX1(double pipX1) {
		this.pipX1 = pipX1;
	}

	public double getPipX2() {
		return pipX2;
	}

	public void setPipX2(double pipX2) {
		this.pipX2 = pipX2;
	}

	public double getPipY1() {
		return pipY1;
	}

	public void setPipY1(double pipY1) {
		this.pipY1 = pipY1;
	}

	public double getPipY2() {
		return pipY2;
	}

	public void setPipY2(double pipY2) {
		this.pipY2 = pipY2;
	}

	public double getRawThermalImageWidth() {
		return rawThermalImageWidth;
	}

	public void setRawThermalImageWidth(double rawThermalImageWidth) {
		this.rawThermalImageWidth = rawThermalImageWidth;
	}

	public double getRawThermalImageHeight() {
		return rawThermalImageHeight;
	}

	public void setRawThermalImageHeight(double rawThermalImageHeight) {
		this.rawThermalImageHeight = rawThermalImageHeight;
	}

	@Override
	public String toString() {
		return "FlirMetadataParam [planckR1=" + planckR1 + ", planckB=" + planckB + ", planckF=" + planckF
				+ ", planckO=" + planckO + ", planckR2=" + planckR2 + ", emissivity=" + emissivity + ", objectDistance="
				+ objectDistance + ", atmosphericTemperature=" + atmosphericTemperature
				+ ", reflectedApparentTemperature=" + reflectedApparentTemperature + ", relativeHumidity="
				+ relativeHumidity + ", irWindowTemperature=" + irWindowTemperature + ", irWindowTransmission="
				+ irWindowTransmission + ", atmosphericTransAlpha1=" + atmosphericTransAlpha1
				+ ", atmosphericTransAlpha2=" + atmosphericTransAlpha2 + ", atmosphericTransBeta1="
				+ atmosphericTransBeta1 + ", atmosphericTransBeta2=" + atmosphericTransBeta2 + ", atmosphericTransX="
				+ atmosphericTransX + ", tau=" + tau + ", R=" + R + ", cameraTemperatureRangeMin="
				+ cameraTemperatureRangeMin + ", cameraTemperatureRangeMax=" + cameraTemperatureRangeMax
				+ ", rawValueRangeMin=" + rawValueRangeMin + ", rawValueRangeMax=" + rawValueRangeMax + ", real2IR="
				+ real2IR + ", offsetX=" + offsetX + ", offsetY=" + offsetY + ", pipX1=" + pipX1 + ", pipX2=" + pipX2
				+ ", pipY1=" + pipY1 + ", pipY2=" + pipY2 + ", rawThermalImageWidth=" + rawThermalImageWidth
				+ ", rawThermalImageHeight=" + rawThermalImageHeight + "]";
	}

}
