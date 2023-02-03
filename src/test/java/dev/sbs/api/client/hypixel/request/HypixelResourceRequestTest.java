package dev.sbs.api.client.hypixel.request;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.resource.ResourceCollectionsResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceItemsResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceSkillsResponse;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class HypixelResourceRequestTest {

    private static final HypixelResourceRequest HYPIXEL_RESOURCE_REQUEST;

    static {
        HYPIXEL_RESOURCE_REQUEST = SimplifiedApi.getWebApi(HypixelResourceRequest.class);
    }

    @Test
    public void getSkills_ok() {
        ResourceSkillsResponse skills = HYPIXEL_RESOURCE_REQUEST.getSkills();
        MatcherAssert.assertThat(skills.getSkills().size(), Matchers.greaterThan(0));
    }

    @Test
    public void getCollections_ok() {
        ResourceCollectionsResponse collections = HYPIXEL_RESOURCE_REQUEST.getCollections();
        MatcherAssert.assertThat(collections.getCollections().size(), Matchers.greaterThan(0));
    }

    @Test
    public void getItems_ok() {
        ResourceItemsResponse items = HYPIXEL_RESOURCE_REQUEST.getItems();
        MatcherAssert.assertThat(items.getItems().size(), Matchers.greaterThan(0));
    }

}
