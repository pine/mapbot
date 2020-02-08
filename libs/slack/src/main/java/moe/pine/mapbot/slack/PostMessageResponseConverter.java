package moe.pine.mapbot.slack;

import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;

class PostMessageResponseConverter {
    PostMessageResponse convert(ChatPostMessageResponse response) {
        return PostMessageResponse.builder()
            .channel(response.getChannel())
            .ts(response.getTs())
            .build();
    }
}
