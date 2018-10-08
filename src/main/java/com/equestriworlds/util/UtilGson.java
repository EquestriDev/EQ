/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  com.google.gson.ExclusionStrategy
 *  com.google.gson.FieldAttributes
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.TypeAdapter
 *  com.google.gson.annotations.Expose
 *  com.google.gson.reflect.TypeToken
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
 *  net.minecraft.server.v1_12_R1.Item
 *  net.minecraft.server.v1_12_R1.ItemStack
 *  net.minecraft.server.v1_12_R1.MojangsonParseException
 *  net.minecraft.server.v1_12_R1.MojangsonParser
 *  net.minecraft.server.v1_12_R1.NBTBase
 *  net.minecraft.server.v1_12_R1.NBTTagCompound
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 *  org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
 *  org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.equestriworlds.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.MojangsonParseException;
import net.minecraft.server.v1_12_R1.MojangsonParser;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UtilGson {
    private static Gson g = new Gson();
    private static final String CLASS_KEY = "SERIAL-ADAPTER-CLASS-KEY";
    private static Gson prettyGson;
    private static Gson compactGson;

    public static Gson getPrettyGson() {
        if (prettyGson == null) {
            prettyGson = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).registerTypeHierarchyAdapter(ItemStack.class, (Object)new ItemStackGsonAdapter()).registerTypeAdapter(PotionEffect.class, (Object)new PotionEffectGsonAdapter()).registerTypeAdapter(Location.class, (Object)new LocationGsonAdapter()).registerTypeAdapter(Date.class, (Object)new DateGsonAdapter()).setPrettyPrinting().disableHtmlEscaping().create();
        }
        return prettyGson;
    }

    public static Gson getCompactGson() {
        if (compactGson == null) {
            compactGson = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).registerTypeHierarchyAdapter(ItemStack.class, (Object)new ItemStackGsonAdapter()).registerTypeAdapter(PotionEffect.class, (Object)new PotionEffectGsonAdapter()).registerTypeAdapter(Location.class, (Object)new LocationGsonAdapter()).registerTypeAdapter(Date.class, (Object)new DateGsonAdapter()).disableHtmlEscaping().create();
        }
        return compactGson;
    }

    public static Gson getNewGson(boolean prettyPrinting) {
        GsonBuilder builder = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).registerTypeHierarchyAdapter(ItemStack.class, (Object)new NewItemStackAdapter()).disableHtmlEscaping();
        if (prettyPrinting) {
            builder.setPrettyPrinting();
        }
        return builder.create();
    }

    private static Map<String, Object> recursiveSerialization(ConfigurationSerializable o) {
        Map<String, Object> originalMap = o.serialize();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            Object o2 = entry.getValue();
            if (!(o2 instanceof ConfigurationSerializable)) continue;
            ConfigurationSerializable serializable = (ConfigurationSerializable)o2;
            Map<String, Object> newMap = UtilGson.recursiveSerialization(serializable);
            newMap.put(CLASS_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
            map.put((String)entry.getKey(), newMap);
        }
        map.put(CLASS_KEY, ConfigurationSerialization.getAlias(o.getClass()));
        return map;
    }

    private static Map<String, Object> recursiveDoubleToInteger(Map<String, Object> originalMap) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            Object o = entry.getValue();
            if (o instanceof Double) {
                Double d = (Double)o;
                Integer i = d.intValue();
                map.put(entry.getKey(), i);
                continue;
            }
            if (o instanceof Map) {
                Map subMap = (Map)o;
                map.put(entry.getKey(), UtilGson.recursiveDoubleToInteger(subMap));
                continue;
            }
            map.put(entry.getKey(), o);
        }
        return map;
    }

    private static String nbtToString(NBTBase base) {
        return base.toString().replace(",}", "}").replace(",]", "]").replaceAll("[0-9]+\\:", "");
    }

    private static net.minecraft.server.v1_12_R1.ItemStack removeSlot(ItemStack item) {
        if (item == null) {
            return null;
        }
        net.minecraft.server.v1_12_R1.ItemStack nmsi = CraftItemStack.asNMSCopy((ItemStack)item);
        if (nmsi == null) {
            return null;
        }
        NBTTagCompound nbtt = nmsi.getTag();
        if (nbtt != null) {
            nbtt.remove("Slot");
            nmsi.setTag(nbtt);
        }
        return nmsi;
    }

    private static ItemStack removeSlotNBT(ItemStack item) {
        if (item == null) {
            return null;
        }
        net.minecraft.server.v1_12_R1.ItemStack nmsi = CraftItemStack.asNMSCopy((ItemStack)item);
        if (nmsi == null) {
            return null;
        }
        NBTTagCompound nbtt = nmsi.getTag();
        if (nbtt != null) {
            nbtt.remove("Slot");
            nmsi.setTag(nbtt);
        }
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)nmsi);
    }

    private static class DateGsonAdapter
    extends TypeAdapter<Date> {
        private DateGsonAdapter() {
        }

        public void write(JsonWriter jsonWriter, Date date) throws IOException {
            if (date == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(date.getTime());
        }

        public Date read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return new Date(jsonReader.nextLong());
        }
    }

    private static class LocationGsonAdapter
    extends TypeAdapter<Location> {
        private static Type seriType = new TypeToken<Map<String, Object>>(){}.getType();
        private static String UUID = "uuid";
        private static String X = "x";
        private static String Y = "y";
        private static String Z = "z";
        private static String YAW = "yaw";
        private static String PITCH = "pitch";

        private LocationGsonAdapter() {
        }

        public void write(JsonWriter jsonWriter, Location location) throws IOException {
            if (location == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(location));
        }

        public Location read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }

        private String getRaw(Location location) {
            HashMap<String, String> serial = new HashMap<String, String>();
            serial.put(UUID, location.getWorld().getUID().toString());
            serial.put(X, Double.toString(location.getX()));
            serial.put(Y, Double.toString(location.getY()));
            serial.put(Z, Double.toString(location.getZ()));
            serial.put(YAW, Float.toString(location.getYaw()));
            serial.put(PITCH, Float.toString(location.getPitch()));
            return g.toJson(serial);
        }

        private Location fromRaw(String raw) {
            Map keys = (Map)g.fromJson(raw, seriType);
            World w = Bukkit.getWorld(java.util.UUID.fromString((String)keys.get(UUID)));
            return new Location(w, Double.parseDouble((String)keys.get(X)), Double.parseDouble((String)keys.get(Y)), Double.parseDouble((String)keys.get(Z)), Float.parseFloat((String)keys.get(YAW)), Float.parseFloat((String)keys.get(PITCH)));
        }

    }

    private static class PotionEffectGsonAdapter
    extends TypeAdapter<PotionEffect> {
        private static Type seriType = new TypeToken<Map<String, Object>>(){}.getType();
        private static String TYPE = "effect";
        private static String DURATION = "duration";
        private static String AMPLIFIER = "amplifier";
        private static String AMBIENT = "ambient";

        private PotionEffectGsonAdapter() {
        }

        public void write(JsonWriter jsonWriter, PotionEffect potionEffect) throws IOException {
            if (potionEffect == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(potionEffect));
        }

        public PotionEffect read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }

        private String getRaw(PotionEffect potion) {
            Map serial = potion.serialize();
            return g.toJson((Object)serial);
        }

        private PotionEffect fromRaw(String raw) {
            Map keys = (Map)g.fromJson(raw, seriType);
            return new PotionEffect(PotionEffectType.getById((int)((Double)keys.get(TYPE)).intValue()), ((Double)keys.get(DURATION)).intValue(), ((Double)keys.get(AMPLIFIER)).intValue(), ((Boolean)keys.get(AMBIENT)).booleanValue());
        }

    }

    private static class ItemStackGsonAdapter
    extends TypeAdapter<ItemStack> {
        private static Type seriType = new TypeToken<Map<String, Object>>(){}.getType();

        private ItemStackGsonAdapter() {
        }

        public void write(JsonWriter jsonWriter, ItemStack itemStack) throws IOException {
            if (itemStack == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(UtilGson.removeSlotNBT(itemStack)));
        }

        public ItemStack read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }

        private String getRaw(ItemStack item) {
            Map serial = item.serialize();
            if (serial.get("meta") != null) {
                ItemMeta itemMeta = item.getItemMeta();
                Map<String, Object> originalMeta = itemMeta.serialize();
                Map<String, Object> meta = new HashMap();
                for (Map.Entry<String, Object> entry : originalMeta.entrySet()) {
                    meta.put(entry.getKey(), entry.getValue());
                }
                for (Map.Entry<String, Object> entry : meta.entrySet()) {
                    Object o = entry.getValue();
                    if (!(o instanceof ConfigurationSerializable)) continue;
                    ConfigurationSerializable serializable = (ConfigurationSerializable)o;
                    Map serialized = UtilGson.recursiveSerialization(serializable);
                    meta.put(entry.getKey(), serialized);
                }
                serial.put("meta", meta);
            }
            return g.toJson((Object)serial);
        }

        private ItemStack fromRaw(String raw) {
            ItemStack item;
            Map keys = (Map)g.fromJson(raw, seriType);
            if (keys.get("amount") != null) {
                Double d = (Double)keys.get("amount");
                Integer i = d.intValue();
                keys.put("amount", i);
            }
            try {
                item = ItemStack.deserialize((Map)keys);
            }
            catch (Exception e) {
                return null;
            }
            if (item == null) {
                return null;
            }
            if (keys.containsKey("meta")) {
                Map itemmeta = (Map)keys.get("meta");
                itemmeta = UtilGson.recursiveDoubleToInteger(itemmeta);
                ItemMeta meta = (ItemMeta)ConfigurationSerialization.deserializeObject((Map)itemmeta, (Class)ConfigurationSerialization.getClassByAlias((String)"ItemMeta"));
                item.setItemMeta(meta);
            }
            return item;
        }

    }

    private static class NewItemStackAdapter
    extends TypeAdapter<ItemStack> {
        private NewItemStackAdapter() {
        }

        public void write(JsonWriter jsonWriter, ItemStack itemStack) throws IOException {
            if (itemStack == null) {
                jsonWriter.nullValue();
                return;
            }
            net.minecraft.server.v1_12_R1.ItemStack item = UtilGson.removeSlot(itemStack);
            if (item == null) {
                jsonWriter.nullValue();
                return;
            }
            try {
                jsonWriter.beginObject();
                jsonWriter.name("type");
                jsonWriter.value(itemStack.getType().toString());
                jsonWriter.name("amount");
                jsonWriter.value((long)itemStack.getAmount());
                jsonWriter.name("data");
                jsonWriter.value((long)itemStack.getDurability());
                jsonWriter.name("tag");
                if (item != null && item.getTag() != null) {
                    jsonWriter.value(UtilGson.nbtToString((NBTBase)item.getTag()));
                } else {
                    jsonWriter.value("");
                }
                jsonWriter.endObject();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public ItemStack read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                return null;
            }
            jsonReader.beginObject();
            jsonReader.nextName();
            Material type = Material.getMaterial((String)jsonReader.nextString());
            jsonReader.nextName();
            int amount = jsonReader.nextInt();
            jsonReader.nextName();
            int data = jsonReader.nextInt();
            net.minecraft.server.v1_12_R1.ItemStack item = new net.minecraft.server.v1_12_R1.ItemStack(CraftMagicNumbers.getItem((Material)type), amount, data);
            jsonReader.nextName();
            String next = jsonReader.nextString();
            if (next.startsWith("{")) {
                NBTTagCompound compound = null;
                try {
                    compound = MojangsonParser.parse((String)ChatColor.translateAlternateColorCodes((char)'&', (String)next));
                }
                catch (MojangsonParseException e) {
                    e.printStackTrace();
                }
                item.setTag(compound);
            }
            jsonReader.endObject();
            return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)item);
        }
    }

    private static class ExposeExlusion
    implements ExclusionStrategy {
        private ExposeExlusion() {
        }

        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            Ignore ignore = (Ignore)fieldAttributes.getAnnotation(Ignore.class);
            if (ignore != null) {
                return true;
            }
            Expose expose = (Expose)fieldAttributes.getAnnotation(Expose.class);
            return expose != null && (!expose.serialize() || !expose.deserialize());
        }

        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.FIELD})
    public static @interface Ignore {
    }

}
