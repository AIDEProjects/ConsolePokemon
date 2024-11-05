package consolepokemon.examples.v0_4_1;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

class Yabi implements JsonSerializer<Yabi>, JsonDeserializer<Yabi> {
    public Yabi instance; // 指向自身的实例
    public String name;

    public Yabi(String name) {
        this.name = name;
        this.instance = this; // 指向自身
    }

    @Override
    public JsonElement serialize(Yabi src, Type typeOfSrc, JsonSerializationContext context) {
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
    public Yabi deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        Yabi yabi = new Yabi(name);

        // 处理 instance 字段，判断其类型
        if (jsonObject.has("instance")) {
            JsonElement instanceElement = jsonObject.get("instance");
            if (instanceElement.isJsonPrimitive() && instanceElement.getAsJsonPrimitive().isString()) {
                // 处理字符串 "self"
                String instanceValue = instanceElement.getAsString();
                if ("self".equals(instanceValue)) {
                    yabi.instance = yabi; // 设置为自身引用
                }
            } else {
                // 处理 Yabi 对象
                yabi.instance = context.deserialize(instanceElement, Yabi.class);
            }
        }
        return yabi;
    }
}

class Trainer {
    public LinkedHashMap<Long, Yabi> yabiMap = new LinkedHashMap<>();

    public void addYabi(Long id, Yabi yabi) {
        yabiMap.put(id, yabi);
    }
}

public class TestCustomSerialize2 {
    public TestCustomSerialize2() {
        GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        // 创建 Trainer 实例并添加 Yabi
        Trainer trainer = new Trainer();
        Yabi yabi1 = new Yabi("Yabi1");
        Yabi yabi2 = new Yabi("Yabi2");
        trainer.addYabi(1L, yabi1);
        trainer.addYabi(2L, yabi2);

		
        yabi1.instance = new Yabi("innerYabi1");
        yabi2.instance = new Yabi("innerYabi2");

        // 序列化 Trainer
        String json = gson.toJson(trainer).replace("  ", "\t");
        System.out.println("Serialized JSON: " + json);

        // 反序列化 Trainer
        Trainer deserializedTrainer = gson.fromJson(json, Trainer.class);
        System.out.println("Deserialized Trainer yabiMap: " + deserializedTrainer.yabiMap);
		
		//使用常规 for 循环遍历 yabiMap
        for (Long id : deserializedTrainer.yabiMap.keySet()) {
            Yabi yabi = deserializedTrainer.yabiMap.get(id);
            System.out.println("ID: " + id + ", Name: " + yabi.name);
            System.out.println("Is instance self-referencing? " + (yabi.instance == yabi)); // 检查自引用
        }
    }
}

