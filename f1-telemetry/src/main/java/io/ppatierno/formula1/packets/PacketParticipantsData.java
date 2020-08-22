/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.packets;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.ppatierno.formula1.PacketConstants;
import io.ppatierno.formula1.PacketUtils;
import io.ppatierno.formula1.enums.Driver;
import io.ppatierno.formula1.enums.Nationality;
import io.ppatierno.formula1.enums.Team;

/**
 * Participants Packet
 * 
 * This is a list of participants in the race. If the vehicle is controlled by
 * AI, then the name will be the driver name. If this is a multiplayer game, the
 * names will be the Steam Id on PC, or the LAN name if appropriate.
 * Frequency: Every 5 seconds
 */
public class PacketParticipantsData extends Packet {

    public static final int SIZE = 1213;
    
    private short numActiveCars;
    private List<ParticipantData> participants = new ArrayList<>(PacketConstants.CARS);

    /**
     * @return Number of active cars in the data – should match number of cats on HUD
     */
    public short getNumActiveCars() {
        return numActiveCars;
    }

    public void setNumActiveCars(short numActiveCars) {
        this.numActiveCars = numActiveCars;
    }

    /**
     * @return Participants
     */
    public List<ParticipantData> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantData> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Participants[");
        sb.append(super.toString());
        sb.append(",numActiveCars=" + this.numActiveCars);
        sb.append(",participants=");
        for (ParticipantData p : participants) {
            sb.append(p.toString() + ",");
        }
        sb.replace(sb.length() - 1, sb.length() - 1, "]");
        return sb.toString();
    }

    @Override
    public Packet fill(ByteBuf buffer) {
        super.fill(buffer);
        this.numActiveCars = buffer.readUnsignedByte();
        for (int i = 0; i < this.numActiveCars; i++) {
            ParticipantData pd = new ParticipantData();
            this.participants.add(pd.fill(buffer));
        }
        return this;
    }

    @Override
    public ByteBuf fillBuffer(ByteBuf buffer) {
        super.fillBuffer(buffer);
        buffer.writeByte(this.numActiveCars);
        for (ParticipantData pd : this.participants) {
            pd.fillBuffer(buffer);
        }
        return buffer;
    }

    public class ParticipantData {

        public static final int NAME_LENGTH = 48;

        private short aiControlled;
        private Driver driverId;
        private Team teamId;
        private short raceNumber;
        private Nationality nationality;
        private String name;
        private short yourTelemetry;

        /**
         * Fill the current ParticipantData with the raw bytes representation
         * 
         * @param buffer buffer with the raw bytes representation
         * @return current filled ParticipantData instance
         */
        public ParticipantData fill(ByteBuf buffer) {
            this.aiControlled = buffer.readUnsignedByte();
            this.driverId = Driver.valueOf(buffer.readUnsignedByte());
            this.teamId = Team.valueOf(buffer.readUnsignedByte());
            this.raceNumber = buffer.readUnsignedByte();
            this.nationality = Nationality.valueOf(buffer.readUnsignedByte());
            this.name = PacketUtils.readString(buffer, ParticipantData.NAME_LENGTH);
            this.yourTelemetry = buffer.readUnsignedByte();
            return this;
        }

        /**
         * Fill the buffer with the raw bytes representation of the current ParticipantData instance
         * 
         * @param buffer buffer to fill
         * @return filled buffer
         */
        public ByteBuf fillBuffer(ByteBuf buffer) {
            buffer.writeByte(this.aiControlled);
            buffer.writeByte(this.driverId.getValue());
            buffer.writeByte(this.teamId.getValue());
            buffer.writeByte(this.raceNumber);
            buffer.writeByte(this.nationality.getValue());
            PacketUtils.writeString(this.name, buffer, ParticipantData.NAME_LENGTH);
            buffer.writeByte(this.yourTelemetry);
            return buffer;
        }

        /**
         * @return Whether the vehicle is AI (1) or Human (0) controlled
         */
        public short getAiControlled() {
            return aiControlled;
        }

        public void setAiControlled(short aiControlled) {
            this.aiControlled = aiControlled;
        }

        /**
         * @return Driver Id
         */
        public Driver getDriverId() {
            return driverId;
        }

        public void setDriverId(Driver driverId) {
            this.driverId = driverId;
        }

        /**
         * @return Team Id
         */
        public Team getTeamId() {
            return teamId;
        }

        public void setTeamId(Team teamId) {
            this.teamId = teamId;
        }

        /**
         * @return Race number of the car
         */
        public short getRaceNumber() {
            return raceNumber;
        }

        public void setRaceNumber(short raceNumber) {
            this.raceNumber = raceNumber;
        }

        /**
         * @return Nationality of the driver
         */
        public Nationality getNationality() {
            return nationality;
        }

        public void setNationality(Nationality nationality) {
            this.nationality = nationality;
        }

        /**
         * @return Name of participant in UTF-8 format – null terminated
         * Will be truncated with … (U+2026) if too long
         */
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The player's UDP setting, 0 = restricted, 1 = public
         */
        public short getYourTelemetry() {
            return yourTelemetry;
        }

        public void setYourTelemetry(short yourTelemetry) {
            this.yourTelemetry = yourTelemetry;
        }

        @Override
        public String toString() {
            return "ParticipantData[aiControlled=" + this.aiControlled +
                    ",driverId=" + this.driverId +
                    ",teamId=" + this.teamId +
                    ",raceNumber=" + this.raceNumber +
                    ",nationality=" + this.nationality +
                    ",name=" + this.name +
                    ",yourTelemetry=" + this.yourTelemetry +
                    "]";
        }
    }
}