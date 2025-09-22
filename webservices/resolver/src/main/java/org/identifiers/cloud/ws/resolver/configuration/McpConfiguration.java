package org.identifiers.cloud.ws.resolver.configuration;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema.*;
import org.identifiers.cloud.ws.resolver.services.mcp.CurieResolutionMcpTool;
import org.identifiers.cloud.ws.resolver.services.mcp.UrlConversionMcpTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpConfiguration {
    @Bean
    public ToolCallbackProvider resolverMcpTools(
            final UrlConversionMcpTool urlConversionMcpTool,
            final CurieResolutionMcpTool curieResolutionMcpTool
    ) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(urlConversionMcpTool, curieResolutionMcpTool)
                .build();
    }

    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> curieResolutionPrompt() {
            return List.of(
                resolveAllCurieUrlsPrompt(),
                resolveBestCurieUrlPrompt(),
                convertUrlToIdorgUriPrompt()
        );
    }

    private McpServerFeatures.SyncPromptSpecification resolveBestCurieUrlPrompt() {
        var arguments = List.of(
                new PromptArgument("curie", "identifiers.org CURIE or compact identifier to resolve", true)
        );
        var prompt = new Prompt("resolve-best-curie-url",
                "Find best valid provider URLs for an identifiers.org CURIE",
                arguments);

        return new McpServerFeatures.SyncPromptSpecification(prompt,
                (exchange, getPromptRequest) -> {
                    String curieArgument = (String) getPromptRequest.arguments().get("curie");
                    var userMessage = new PromptMessage(Role.USER, new TextContent("Please the best valid provider URL for CURIE '"+curieArgument+"'"));
                    return new GetPromptResult("Best provider URL", List.of(userMessage));
                }
        );
    }

    private McpServerFeatures.SyncPromptSpecification resolveAllCurieUrlsPrompt() {
        var arguments = List.of(
                new PromptArgument("curie", "identifiers.org CURIE or compact identifier to resolve", true)
        );
        var prompt = new Prompt("resolve-all-curie-urls",
                "Find all valid provider URLs for an identifiers.org CURIE",
                arguments);

        return new McpServerFeatures.SyncPromptSpecification(prompt,
                (exchange, getPromptRequest) -> {
                    String curieArgument = (String) getPromptRequest.arguments().get("curie");
                    var userMessage = new PromptMessage(Role.USER, new TextContent("Please list valid provider URLs for CURIE '"+curieArgument+"'"));
                    return new GetPromptResult("List of provider URLs", List.of(userMessage));
                }
        );
    }

    private McpServerFeatures.SyncPromptSpecification convertUrlToIdorgUriPrompt() {
        var arguments = List.of(
                new PromptArgument("url", "Provider URL", true)
        );
        var prompt = new Prompt("convert-url-to-idorg-uri",
                "Convert URL to identifiers.org URI",
                arguments);

        return new McpServerFeatures.SyncPromptSpecification(prompt,
                (exchange, getPromptRequest) -> {
                    String curieArgument = (String) getPromptRequest.arguments().get("curie");
                    var userMessage = new PromptMessage(Role.USER, new TextContent("Please convert the URL '"+curieArgument+"' to an identifiers.org URI"));
                    return new GetPromptResult("Identifiers.org URI that references the URL given", List.of(userMessage));
                }
        );
    }
}
