package info.androidhive.firebase;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Logic {

    userProfile up;
    calData cd;

    Logic() {
        up = new userProfile("n","e",19,0.0f,0.0f,true,'l');
        float i = up.setCaloriesNeeded();
        cd = new calData(true,40,"none");
    }

    public void change(String s) {
        String[] p = this.parse(s);
        this.cd.update(p);
        up.addCalorieCount(cd.count*cd.getCal());
    }


     class userProfile {
        String name;
        String email;
        int age;
        float height;
        float weight;
        boolean gender;
        char activity;
        //float goal;
        float dailyRecommendedCal;
        float caloriesConsumedToday;


        public userProfile(String n, String e, int a, float h, float w,boolean g, char act){
            name = "kefei su";
            email = "kefeisu@gmail.com";
            age = 19;
            height = 174.0f;
            weight = 63.0f;
            gender = true;// true for male; false for female
            activity = 'M';
            //goal = 0.0f;
            dailyRecommendedCal = 0.0f;
            caloriesConsumedToday = 0.0f;
        }

        public float setCaloriesNeeded() {
            float factor = 0;
            if (activity == 'L' || activity == 'l'){
                factor = 1.2f;
            }
            if (activity == 'M' || activity == 'm') {
                factor = 1.3f;
            }
            if (activity == 'H' || activity == 'h') {
                factor = 1.4f;
            }
            if (gender == true) {
                this.dailyRecommendedCal = (66.5f + (13.8f * weight) + (5f * height) - (6.8f * age)) * factor;
            }
            if (gender == false) {
                this.dailyRecommendedCal = (655.1f + (9.6f * weight) + (1.9f * height) - (4.7f * age)) * factor;
            }
            return dailyRecommendedCal;
        }

        public String getDaily(){
            return String.valueOf(dailyRecommendedCal);
        }

         public String getCurrent() {
             return String.valueOf(caloriesConsumedToday);
         }

        public void addCalorieCount(float caloriesConsumed) {
            caloriesConsumedToday += caloriesConsumed;
        }

        public void burnCalories(float caloriesBurned) {
            caloriesConsumedToday -= caloriesBurned;
        }

        public boolean overate(){
            return (caloriesConsumedToday > dailyRecommendedCal);
        }

        public void resetCalories () {
            caloriesConsumedToday = 0;
        }

         public float getPercent(){
             return (caloriesConsumedToday/dailyRecommendedCal)*100;
         }
    }

    class calData {
        boolean add;
        int count;
        String food;
        database d;

        calData(boolean a, int c, String f) {
            this.add = a;
            this.count = c;
            this.food = f;
            this.d = new database();
        }

        String getFood(){
            return food;
        }

        int getCal() {
            return d.lookup(food);
        }

        public void update(String[] parsedSentence) {
            add =  true;
            count = 1;
            food = "none";
            for (int i = 0; i < parsedSentence.length; ++i) {
                if (parsedSentence[i] == "burned" ||
                        parsedSentence[i] == "burn") {
                    add = false;
                } else
                if (isNumeric(parsedSentence[i])) {
                    count = Integer.parseInt(parsedSentence[i]);
                } else
                if (d.contains(parsedSentence[i])) {
                    food = parsedSentence[i];
                }
            }
            return;
        }

        public String getReply() {
            if(food == "none") {return " ";}
            if (food == "calories") {
                return (String.valueOf(this.count) + " calories was added to your daily intake");
            } else {
                String s = String.valueOf(this.getCal());
                String p = String.valueOf(this.getCal() * this.count);
                return (this.food + " has about " + s + " calories;\n" + p + " calories was added to your daily intake");
            }
        }
    }


    class database {
        Map<String, Integer> referenceTable = new HashMap<String, Integer>();

        database(){
            referenceTable.put("none", 0);
            referenceTable.put("calories", 1);
            referenceTable.put("apple", 72 );
            referenceTable.put("apples", 72 );
            referenceTable.put("bagel",289 );
            referenceTable.put("banana", 105);
            referenceTable.put("bananas", 105);
            referenceTable.put("beer",153 );
            referenceTable.put("bread", 66 );
            referenceTable.put("butter",102 );
            referenceTable.put("carrots", 52 );
            referenceTable.put("cheese", 113 );
            referenceTable.put("chicken", 142 );
            referenceTable.put("chili", 287 );
            referenceTable.put("cookie", 59);
            referenceTable.put("cookies", 59);
            referenceTable.put("coffee", 2 );
            referenceTable.put("coke", 136 );
            referenceTable.put("cola", 136 );
            referenceTable.put("corn",180 );
            referenceTable.put("egg",102 );
            referenceTable.put("eggs",102 );
            referenceTable.put("cracker", 59 );
            referenceTable.put("granola" + "bar", 193 );
            referenceTable.put("green" + "beans", 40 );
            referenceTable.put("ground" + "beef", 193 );
            referenceTable.put("hot" + "dog", 137);
            referenceTable.put("ice" + "cream", 145 );
            referenceTable.put("donut", 289 );
            referenceTable.put("milk", 122 );
            referenceTable.put("nuts", 168);
            referenceTable.put("oatmeal", 147);
            referenceTable.put("orange" + "juice", 112);
            referenceTable.put("pizza", 298);
            referenceTable.put("pizzas", 298);
            referenceTable.put("pork" + "chop", 221);
            referenceTable.put("potato" + "chips", 155);
            referenceTable.put("Pretzels", 108);
            referenceTable.put("raisins", 130);
            referenceTable.put("red" + "wine", 123);
            referenceTable.put("rice", 205);
            referenceTable.put("shrimp", 84);
            referenceTable.put("spaghetti", 221);
            referenceTable.put("tuna", 100);
            referenceTable.put("white" + "wine", 121);
            referenceTable.put("cake", 243);
            referenceTable.put("chocolate", 598);
            referenceTable.put("tahini", 89);
            referenceTable.put("rice" + "cakes", 129);
            referenceTable.put("dates", 23);
            referenceTable.put("chickpeas", 286);
            referenceTable.put("ramen" + "noodles", 380);
            referenceTable.put("salmon", 208);
            referenceTable.put("beef", 332);
            referenceTable.put("bacon", 548);
            referenceTable.put("potato" + "salad", 143);
            referenceTable.put("lard", 902);
            referenceTable.put("turkey", 900);
            referenceTable.put("tortilla" + "chips", 465);
            referenceTable.put("soda", 41);
            referenceTable.put("pasta", 131);
            referenceTable.put("candy", 535);
            referenceTable.put("quinoa", 222);
            referenceTable.put("yogurt", 59);
            referenceTable.put("avocados", 160);
            referenceTable.put("avocado", 160);
            referenceTable.put("honey", 304);
            referenceTable.put("soccer", -622);
            referenceTable.put("basketball", -596);
            referenceTable.put("golf", -820);
            referenceTable.put("football", -950);
            referenceTable.put("cricket", -272);
            referenceTable.put("baseball", -480);
            referenceTable.put("hockey", -700);
            referenceTable.put("volleyball", -360);
            referenceTable.put("swimming", -704);
            referenceTable.put("badminton", -544);
            referenceTable.put("tennis", -760);
            referenceTable.put("ran" + "one" + "mile", -398);
            referenceTable.put("handball", -563);
            referenceTable.put("boxing", -846);
            referenceTable.put("skiing", -532);
            referenceTable.put("table" + "tennis", -700);
            referenceTable.put("polo", 1180);
            referenceTable.put("walk", -240);
        }

        public int lookup(String s){
            return this.referenceTable.get(s);
        }

        public boolean contains(String s) {
            return this.referenceTable.containsKey(s);
        }
    }

    public String[] parse(String sentence) {
        return sentence.split("\\s+");
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

}
