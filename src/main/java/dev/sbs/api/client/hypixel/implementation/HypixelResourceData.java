package dev.sbs.api.client.hypixel.implementation;

import feign.RequestLine;
import dev.sbs.api.client.hypixel.response.resource.ResourceCollectionsResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceItemsResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceSkillsResponse;

public interface HypixelResourceData extends HypixelRequestInterface {

    @RequestLine("GET /resources/skyblock/skills")
    ResourceSkillsResponse getSkills();

    @RequestLine("GET /resources/skyblock/collections")
    ResourceCollectionsResponse getCollections();

    @RequestLine("GET /resources/skyblock/items")
    ResourceItemsResponse getItems();

}