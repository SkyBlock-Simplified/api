package dev.sbs.api.client.hypixel.request;

import dev.sbs.api.client.hypixel.response.resource.ResourceCollectionsResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceElectionResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceItemsResponse;
import dev.sbs.api.client.hypixel.response.resource.ResourceSkillsResponse;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

public interface HypixelResourceRequest extends IHypixelRequest {

    @RequestLine("GET /resources/skyblock/skills")
    @NotNull ResourceSkillsResponse getSkills();

    @RequestLine("GET /resources/skyblock/collections")
    @NotNull ResourceCollectionsResponse getCollections();

    @RequestLine("GET /resources/skyblock/items")
    @NotNull ResourceItemsResponse getItems();

    @RequestLine("GET /resources/skyblock/election")
    @NotNull ResourceElectionResponse getElection();

}
