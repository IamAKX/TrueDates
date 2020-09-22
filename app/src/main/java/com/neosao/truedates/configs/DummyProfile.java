package com.neosao.truedates.configs;

import com.neosao.truedates.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class DummyProfile {
    ArrayList<Profile> ALL_PROFILES = new ArrayList<>();

    public DummyProfile() {
        ALL_PROFILES.add(new Profile(
                1,
                "https://cdn.pixabay.com/photo/2012/02/29/15/40/beautiful-19075_960_720.jpg",
                "Sofia",
                20,
                "New York"
        ));
        ALL_PROFILES.add(new Profile(2,
                "https://cdn.pixabay.com/photo/2017/12/22/14/42/girl-3033718_960_720.jpg",
                "Roma",
                22,
                "California"
        ));
        ALL_PROFILES.add(new Profile(
                3,
                "https://cdn.pixabay.com/photo/2017/02/16/23/10/smile-2072908_960_720.jpg",
                "Zoya",
                23,
                "Atlantic City"
        ));
        ALL_PROFILES.add(new Profile(4,
                "https://cdn.pixabay.com/photo/2017/02/16/23/10/smile-2072907_960_720.jpg",
                "Carol",
                19,
                "New City"
        ));
        ALL_PROFILES.add(new Profile(5,
                "https://cdn.pixabay.com/photo/2016/11/29/13/14/attractive-1869761_960_720.jpg",
                "Saansa",
                20,
                "Manhattan"
        ));
        ALL_PROFILES.add(new Profile(6,
                "https://cdn.pixabay.com/photo/2016/06/06/17/05/model-1439909_960_720.jpg",
                "Khaleesi",
                24,
                "Future Westros"
        ));
        ALL_PROFILES.add(new Profile(7,
                "https://cdn.pixabay.com/photo/2019/06/02/17/33/portrait-4246954_960_720.jpg",
                "Heena",
                21,
                "India"
        ));
        ALL_PROFILES.add(new Profile(8,
                "https://cdn.pixabay.com/photo/2019/11/06/05/57/model-4605248_960_720.jpg",
                "Jasmin",
                23,
                "Taxes"
        ));
        ALL_PROFILES.add(new Profile(9,
                "https://cdn.pixabay.com/photo/2015/09/17/14/24/guitar-944262_960_720.jpg",
                "Monica",
                23,
                "New York"
        ));
        ALL_PROFILES.add(new Profile(10,
                "https://cdn.pixabay.com/photo/2017/07/25/10/37/hair-2537564_960_720.jpg",
                "Phoebe",
                22,
                "New York City"
        ));
        ALL_PROFILES.add(new Profile(11,
                "https://cdn.pixabay.com/photo/2017/07/25/10/36/beauty-2537562_960_720.jpg",
                "Rachel",
                22,
                "Central Perk"
        ));
        ALL_PROFILES.add(new Profile(12,
                "https://cdn.pixabay.com/photo/2015/12/08/00/57/model-woman-1082219_960_720.jpg",
                "Jainis",
                24,
                "New York"
        ));
        ALL_PROFILES.add(new Profile(13,
                "https://cdn.pixabay.com/photo/2015/10/12/15/10/woman-984247_960_720.jpg",
                "Samia",
                19,
                "India"
        ));
        ALL_PROFILES.add(new Profile(14,
                "https://cdn.pixabay.com/photo/2016/01/19/15/23/portrait-woman-1149249_960_720.jpg",
                "Hornol",
                24,
                "Caltech"
        ));
        ALL_PROFILES.add(new Profile(15,
                "https://cdn.pixabay.com/photo/2016/03/23/08/34/beautiful-1274360_960_720.jpg",
                "Korial",
                21,
                "Japan"
        ));
        ALL_PROFILES.add(new Profile(16,
                "https://cdn.pixabay.com/photo/2017/06/15/22/05/girl-2406963_960_720.jpg",
                "Chin Si",
                23,
                "China"
        ));
        ALL_PROFILES.add(new Profile(17,
                "https://cdn.pixabay.com/photo/2016/11/19/17/11/blond-1840386_960_720.jpg",
                "Khloe",
                21,
                "America"
        ));
    }

    public ArrayList<Profile> getAllProfileList() {
        return ALL_PROFILES;
    }
}
