/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.packets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.ppatierno.formula1.PacketConstants;
import io.ppatierno.formula1.enums.DrsAllowed;
import io.ppatierno.formula1.enums.ErsDeployMode;
import io.ppatierno.formula1.enums.FuelMix;
import io.ppatierno.formula1.enums.TractionControl;
import io.ppatierno.formula1.enums.TyreCompound;
import io.ppatierno.formula1.enums.VehicleFiaFlag;

/**
 * Car Status Packet
 * 
 * This packet details car statuses for all the cars in the race. It includes
 * values such as the damage readings on the car.
 */
public class PacketCarStatusData extends Packet {
    
    private List<CarStatusData> carStatusData = new ArrayList<>(PacketConstants.CARS);

    /**
     * @return Car status data for all cars
     */
    public List<CarStatusData> getCarStatusData() {
        return carStatusData;
    }

    public void setCarStatusData(List<CarStatusData> carStatusData) {
        this.carStatusData = carStatusData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CarStatusData[");
        sb.append(super.toString());
        sb.append(",carStatusData=");
        for (CarStatusData c : carStatusData) {
            sb.append(c.toString() + ",");
        }
        sb.replace(sb.length() - 1, sb.length() - 1, "]");
        return sb.toString();
    }

    @Override
    public Packet fill(ByteBuf buffer) {
        super.fill(buffer);
        for (int i = 0; i < PacketConstants.CARS; i++) {
            CarStatusData csd = new CarStatusData();
            csd.setTractionControl(TractionControl.valueOf(buffer.readUnsignedByte()));
            csd.setAntiLockBrakes(buffer.readUnsignedByte());
            csd.setFuelMix(FuelMix.valueOf(buffer.readUnsignedByte()));
            csd.setFrontBrakeBias(buffer.readUnsignedByte());
            csd.setPitLimiterStatus(buffer.readUnsignedByte());
            csd.setFuelInTank(buffer.readFloatLE());
            csd.setFuelCapacity(buffer.readFloatLE());
            csd.setFuelRemainingLaps(buffer.readFloatLE());
            csd.setMaxRPM(buffer.readUnsignedShortLE());
            csd.setIdleRPM(buffer.readUnsignedShortLE());
            csd.setMaxGears(buffer.readUnsignedByte());
            csd.setDrsAllowed(DrsAllowed.valueOf(buffer.readUnsignedByte()));
            csd.setDrsActivationDistance(buffer.readUnsignedShortLE());
            for (int j = 0; j < PacketConstants.TYRES; j++) {
                csd.getTyresWear()[j] = buffer.readUnsignedByte();
            }
            csd.setActualTyreCompound(TyreCompound.valueOf(buffer.readUnsignedByte()));
            csd.setVisualTyreCompound(TyreCompound.valueOf(buffer.readUnsignedByte()));
            csd.setTyresAgeLaps(buffer.readUnsignedByte());
            for (int j = 0; j < PacketConstants.TYRES; j++) {
                csd.getTyresDamage()[j] = buffer.readUnsignedByte();
            }
            csd.setFrontLeftWingDamage(buffer.readUnsignedByte());
            csd.setFrontRightWingDamage(buffer.readUnsignedByte());
            csd.setRearWingDamage(buffer.readUnsignedByte());
            csd.setDrsFault(buffer.readUnsignedByte());
            csd.setEngineDamage(buffer.readUnsignedByte());
            csd.setGearBoxDamage(buffer.readUnsignedByte());
            csd.setVehicleFiaFlags(VehicleFiaFlag.valueOf(buffer.readByte()));
            csd.setErsStoreEnergy(buffer.readFloatLE());
            csd.setErsDeployMode(ErsDeployMode.valueOf(buffer.readUnsignedByte()));
            csd.setErsHarvestedThisLapMGUK(buffer.readFloatLE());
            csd.setErsHarvestedThisLapMGUH(buffer.readFloatLE());
            csd.setErsDeployedThisLap(buffer.readFloatLE());
            this.carStatusData.add(csd);
        }
        return this;
    }

    class CarStatusData {

        private TractionControl tractionControl;
        private short antiLockBrakes;
        private FuelMix fuelMix;
        private short frontBrakeBias;
        private short pitLimiterStatus;
        private float fuelInTank;
        private float fuelCapacity;
        private float fuelRemainingLaps;
        private int maxRPM;
        private int idleRPM;
        private short maxGears;
        private DrsAllowed drsAllowed;
        private int drsActivationDistance;
        private short tyresWear[] = new short[PacketConstants.TYRES];
        private TyreCompound actualTyreCompound;
        private TyreCompound visualTyreCompound;
        private short tyresAgeLaps;
        private short tyresDamage[] = new short[PacketConstants.TYRES];
        private short frontLeftWingDamage;
        private short frontRightWingDamage;
        private short rearWingDamage;
        private short drsFault;
        private short engineDamage;
        private short gearBoxDamage;
        private VehicleFiaFlag vehicleFiaFlags;
        private float ersStoreEnergy;
        private ErsDeployMode ersDeployMode;
        private float ersHarvestedThisLapMGUK;
        private float ersHarvestedThisLapMGUH;
        private float ersDeployedThisLap;

        /**
         * @return Traction control
         * 0 (off) - 2 (high)
         */
        public TractionControl getTractionControl() {
            return tractionControl;
        }

        public void setTractionControl(TractionControl tractionControl) {
            this.tractionControl = tractionControl;
        }

        /**
         * @return Antilock brakes
         * 0 (off) - 1 (on)
         */
        public short getAntiLockBrakes() {
            return antiLockBrakes;
        }

        public void setAntiLockBrakes(short antiLockBrakes) {
            this.antiLockBrakes = antiLockBrakes;
        }

        /**
         * @return Fuel mix
         * 0 = lean, 1 = standard, 2 = rich, 3 = max
         */
        public FuelMix getFuelMix() {
            return fuelMix;
        }

        public void setFuelMix(FuelMix fuelMix) {
            this.fuelMix = fuelMix;
        }

        /**
         * @return Front brake bias (percentage)
         */
        public short getFrontBrakeBias() {
            return frontBrakeBias;
        }

        public void setFrontBrakeBias(short frontBrakeBias) {
            this.frontBrakeBias = frontBrakeBias;
        }

        /**
         * @return Pit limiter status
         * 0 = off, 1 = on
         */
        public short getPitLimiterStatus() {
            return pitLimiterStatus;
        }

        public void setPitLimiterStatus(short pitLimiterStatus) {
            this.pitLimiterStatus = pitLimiterStatus;
        }

        /**
         * @return Current fuel mass
         */
        public float getFuelInTank() {
            return fuelInTank;
        }

        public void setFuelInTank(float fuelInTank) {
            this.fuelInTank = fuelInTank;
        }

        /**
         * @return Fuel capacity
         */
        public float getFuelCapacity() {
            return fuelCapacity;
        }

        public void setFuelCapacity(float fuelCapacity) {
            this.fuelCapacity = fuelCapacity;
        }

        /**
         * @return Fuel remaining in terms of laps (value on MFD)
         */
        public float getFuelRemainingLaps() {
            return fuelRemainingLaps;
        }

        public void setFuelRemainingLaps(float fuelRemainingLaps) {
            this.fuelRemainingLaps = fuelRemainingLaps;
        }

        /**
         * @return Cars max RPM, point of rev limiter
         */
        public int getMaxRPM() {
            return maxRPM;
        }

        public void setMaxRPM(int maxRPM) {
            this.maxRPM = maxRPM;
        }

        /**
         * @return Cars idle RPM
         */
        public int getIdleRPM() {
            return idleRPM;
        }

        public void setIdleRPM(int idleRPM) {
            this.idleRPM = idleRPM;
        }

        /**
         * @return Maximum number of gears
         */
        public short getMaxGears() {
            return maxGears;
        }

        public void setMaxGears(short maxGears) {
            this.maxGears = maxGears;
        }

        /**
         * @return DRS allowed
         * 0 = not allowed, 1 = allowed, -1 = unknown
         */
        public DrsAllowed getDrsAllowed() {
            return drsAllowed;
        }

        public void setDrsAllowed(DrsAllowed drsAllowed) {
            this.drsAllowed = drsAllowed;
        }

        /**
         * @return DRS activation distance
         * 0 = DRS not available, non-zero - DRS will be available in [X] metres
         */
        public int getDrsActivationDistance() {
            return drsActivationDistance;
        }

        public void setDrsActivationDistance(int drsActivationDistance) {
            this.drsActivationDistance = drsActivationDistance;
        }

        /**
         * @return Tyre wear percentage
         */
        public short[] getTyresWear() {
            return tyresWear;
        }

        public void setTyresWear(short[] tyresWear) {
            this.tyresWear = tyresWear;
        }

        /**
         * @return Actual tyre compound
         * F1 Modern - 16 = C5, 17 = C4, 18 = C3, 19 = C2, 20 = C1, 7 = inter, 8 = wet
         * F1 Classic - 9 = dry, 10 = wet
         * F2 – 11 = super soft, 12 = soft, 13 = medium, 14 = hard, 15 = wet
         */
        public TyreCompound getActualTyreCompound() {
            return actualTyreCompound;
        }

        public void setActualTyreCompound(TyreCompound actualTyreCompound) {
            this.actualTyreCompound = actualTyreCompound;
        }

        /**
         * @return Visual tyre compound
         * F1 visual (can be different from actual compound) 16 = soft, 17 = medium, 18 = hard, 7 = inter, 8 = wet
         * F1 Classic – same as above
         * F2 – same as above
         */
        public TyreCompound getVisualTyreCompound() {
            return visualTyreCompound;
        }

        public void setVisualTyreCompound(TyreCompound visualTyreCompound) {
            this.visualTyreCompound = visualTyreCompound;
        }

        /**
         * @return Age in laps of the current set of tyres
         */
        public short getTyresAgeLaps() {
            return tyresAgeLaps;
        }

        public void setTyresAgeLaps(short tyresAgeLaps) {
            this.tyresAgeLaps = tyresAgeLaps;
        }

        /**
         * @return Tyre damage (percentage)
         */
        public short[] getTyresDamage() {
            return tyresDamage;
        }

        public void setTyresDamage(short[] tyresDamage) {
            this.tyresDamage = tyresDamage;
        }

        /**
         * @return Front left wing damage (percentage)
         */
        public short getFrontLeftWingDamage() {
            return frontLeftWingDamage;
        }

        public void setFrontLeftWingDamage(short frontLeftWingDamage) {
            this.frontLeftWingDamage = frontLeftWingDamage;
        }

        /**
         * @return Front right wing damage (percentage)
         */
        public short getFrontRightWingDamage() {
            return frontRightWingDamage;
        }

        public void setFrontRightWingDamage(short frontRightWingDamage) {
            this.frontRightWingDamage = frontRightWingDamage;
        }

        /**
         * @return Rear wing damage (percentage)
         */
        public short getRearWingDamage() {
            return rearWingDamage;
        }

        public void setRearWingDamage(short rearWingDamage) {
            this.rearWingDamage = rearWingDamage;
        }

        /**
         * @return Indicator for DRS fault
         * 0 = OK, 1 = fault
         */
        public short getDrsFault() {
            return drsFault;
        }

        public void setDrsFault(short drsFault) {
            this.drsFault = drsFault;
        }

        /**
         * @return Engine damage (percentage)
         */
        public short getEngineDamage() {
            return engineDamage;
        }

        public void setEngineDamage(short engineDamage) {
            this.engineDamage = engineDamage;
        }

        /**
         * @return Gear box damage (percentage)
         */
        public short getGearBoxDamage() {
            return gearBoxDamage;
        }

        public void setGearBoxDamage(short gearBoxDamage) {
            this.gearBoxDamage = gearBoxDamage;
        }

        /**
         * @return Vehicle FIA flags
         * -1 = invalid/unknown, 0 = none, 1 = green
         * 2 = blue, 3 = yellow, 4 = red
         */
        public VehicleFiaFlag getVehicleFiaFlags() {
            return vehicleFiaFlags;
        }

        public void setVehicleFiaFlags(VehicleFiaFlag vehicleFiaFlags) {
            this.vehicleFiaFlags = vehicleFiaFlags;
        }

        /**
         * @return ERS energy store in Joules
         */
        public float getErsStoreEnergy() {
            return ersStoreEnergy;
        }

        public void setErsStoreEnergy(float ersStoreEnergy) {
            this.ersStoreEnergy = ersStoreEnergy;
        }

        /**
         * @return ERS deploy mode
         * 0 = none, 1 = medium, 2 = overtake, 3 = hotlap
         */
        public ErsDeployMode getErsDeployMode() {
            return ersDeployMode;
        }

        public void setErsDeployMode(ErsDeployMode ersDeployMode) {
            this.ersDeployMode = ersDeployMode;
        }

        /**
         * @return ERS energy harvested this lap by MGU-K
         */
        public float getErsHarvestedThisLapMGUK() {
            return ersHarvestedThisLapMGUK;
        }

        public void setErsHarvestedThisLapMGUK(float ersHarvestedThisLapMGUK) {
            this.ersHarvestedThisLapMGUK = ersHarvestedThisLapMGUK;
        }

        /**
         * @return ERS energy harvested this lap by MGU-H
         */
        public float getErsHarvestedThisLapMGUH() {
            return ersHarvestedThisLapMGUH;
        }

        public void setErsHarvestedThisLapMGUH(float ersHarvestedThisLapMGUH) {
            this.ersHarvestedThisLapMGUH = ersHarvestedThisLapMGUH;
        }

        /**
         * @return ERS energy deployed this lap
         */
        public float getErsDeployedThisLap() {
            return ersDeployedThisLap;
        }

        public void setErsDeployedThisLap(float ersDeployedThisLap) {
            this.ersDeployedThisLap = ersDeployedThisLap;
        }

        @Override
        public String toString() {
            return "CarStatusData[tractionControl=" + this.tractionControl +
                    ",antiLockBrakes=" + this.antiLockBrakes +
                    ",fuelMix=" + this.fuelMix +
                    ",frontBrakeBias=" + this.frontBrakeBias +
                    ",pitLimiterStatus=" + this.pitLimiterStatus +
                    ",fuelInTank=" + this.fuelInTank +
                    ",fuelCapacity=" + this.fuelCapacity +
                    ",fuelRemainingLaps=" + this.fuelRemainingLaps +
                    ",maxRPM=" + this.maxRPM +
                    ",idleRPM=" + this.idleRPM +
                    ",maxGears=" + this.maxGears +
                    ",drsAllowed=" + this.drsAllowed +
                    ",drsActivationDistance=" + this.drsActivationDistance +
                    ",tyresWear=" + Arrays.toString(this.tyresWear) +
                    ",actualTyreCompound=" + this.actualTyreCompound +
                    ",visualTyreCompound=" + this.visualTyreCompound +
                    ",tyresAgeLaps=" + this.tyresAgeLaps +
                    ",tyresDamage=" + Arrays.toString(this.tyresDamage) +
                    ",frontLeftWingDamage=" + this.frontLeftWingDamage +
                    ",frontRightWingDamage=" + this.frontRightWingDamage +
                    ",rearWingDamage=" + this.rearWingDamage +
                    ",drsFault=" + this.drsFault +
                    ",engineDamage=" + this.engineDamage +
                    ",gearBoxDamage=" + this.gearBoxDamage +
                    ",vehicleFiaFlags=" + this.vehicleFiaFlags +
                    ",ersStoreEnergy=" + this.ersStoreEnergy +
                    ",ersDeployMode=" + this.ersDeployMode +
                    ",ersHarvestedThisLapMGUK=" + this.ersHarvestedThisLapMGUK +
                    ",ersHarvestedThisLapMGUH=" + this.ersHarvestedThisLapMGUH +
                    ",ersDeployedThisLap=" + this.ersDeployedThisLap +
                    "]";
        }
    }
}