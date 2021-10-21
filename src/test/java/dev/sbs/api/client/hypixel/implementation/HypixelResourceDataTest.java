package dev.sbs.api.client.hypixel.implementation;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.resource.ResourceCollectionsResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceItemsResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceSkillsResponse;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class HypixelResourceDataTest {

    private static final HypixelResourceData hypixelResourceData;

    static {
        hypixelResourceData = SimplifiedApi.getWebApi(HypixelResourceData.class);
    }

    @Test
    public void getSkills_ok() {
        ResourceSkillsResponse skills = hypixelResourceData.getSkills();
        MatcherAssert.assertThat(skills.getSkills().size(), Matchers.greaterThan(0));
    }

    @Test
    public void getCollections_ok() {
        ResourceCollectionsResponse collections = hypixelResourceData.getCollections();
        MatcherAssert.assertThat(collections.getCollections().size(), Matchers.greaterThan(0));
    }

    @Test
    public void getItems_ok() {
        ResourceItemsResponse items = hypixelResourceData.getItems();
        MatcherAssert.assertThat(items.getItems().size(), Matchers.greaterThan(0));
    }

}
