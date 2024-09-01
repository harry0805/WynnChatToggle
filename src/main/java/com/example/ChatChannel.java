package com.example;

import net.minecraft.util.Formatting;


public class ChatChannel {
    public static All All = new All();
    public static Party Party = new Party();
    public static Guild Guild = new Guild();

    public static abstract class Channel {
        public String id;
        public String name;
        public Formatting formatColor;
        public String formattedName;
        public String command;

        public String getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public Formatting getFormatColor() {
            return formatColor;
        }
        public String getFormattedName() {
            return formattedName;
        }
    }

    public static class All extends Channel {
        public All() {
            id = "all";
            name = "ALL";
            formatColor = Formatting.WHITE;
            formattedName = "§fALL§r";
            command = null;
        }
    }

    public static class Party extends Channel {
        public Party() {
            id = "party";
            name = "PARTY";
            formatColor = Formatting.YELLOW;
            formattedName = "§ePARTY§r";
            command = "p";
        }
    }

    public static class Guild extends Channel {
        public Guild() {
            id = "guild";
            name = "GUILD";
            formatColor = Formatting.AQUA;
            formattedName = "§bGUILD§r";
            command = "g";
        }
    }
}

