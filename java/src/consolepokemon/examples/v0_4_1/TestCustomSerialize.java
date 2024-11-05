package consolepokemon.examples.v0_4_1;

import com.google.gson.*;
import java.lang.reflect.Type;

class Yabi2 implements JsonSerializer<Yabi2>, JsonDeserializer<Yabi2> {
    public Yabi2 instance; // 指向自身的实例
    public String name;

    public Yabi2(String name) {
        this.name = name;
        this.instance = this; // 指向自身
    }
	
	@Override
    public JsonElement serialize(Yabi2 src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.name);

        // 处理 instance 字段以避免循环引用
        if (src.instance == src) {
            jsonObject.addProperty("instance", "self"); // 标记为自引用
        } else {
            jsonObject.add("instance", context.serialize(src.instance));
        }

        return jsonObject;
    }
	
	@Override
    public Yabi2 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        Yabi2 yabi2 = new Yabi2(name);

        // 处理 instance 字段，判断其类型
        if (jsonObject.has("instance")) {
            JsonElement instanceElement = jsonObject.get("instance");
            if (instanceElement.isJsonPrimitive() && instanceElement.getAsJsonPrimitive().isString()) {
                // 处理字符串 "self"
                String instanceValue = instanceElement.getAsString();
                if ("self".equals(instanceValue)) {
                    yabi2.instance = yabi2; // 设置为自身引用
                }
            } else {
                // 处理 Yabi2 对象
                yabi2.instance = context.deserialize(instanceElement, Yabi2.class);
            }
        }
        return yabi2;
    }
}


public class TestCustomSerialize {
    public TestCustomSerialize() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Yabi2 yabi2 = new Yabi2("Example");
		yabi2.instance = new Yabi2("innerExample");

        String json = gson.toJson(yabi2);
        System.out.println("Serialized JSON: " + json);

        Yabi2 deserializedYabi2 = gson.fromJson(json, Yabi2.class);
        System.out.println("Deserialized name: " + deserializedYabi2.name);
        System.out.println("Deserialized instance: " + deserializedYabi2.instance);
        System.out.println("Is instance self-referencing? " + (deserializedYabi2.instance == deserializedYabi2));
    }
}

