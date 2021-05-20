package moe.pine.mapbot.slack;

import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Field;

import java.util.List;
import java.util.stream.Collectors;

class PostMessageRequestConverter {
    ChatPostMessageRequest convert(PostMessageRequest postMessageRequest) {
        List<Field> fields =
                postMessageRequest.getTextFields()
                        .stream()
                        .map(v -> Field.builder()
                                .title(v.title())
                                .value(v.value())
                                .build())
                        .collect(Collectors.toUnmodifiableList());

        Attachment attachment =
                Attachment.builder()
                        .fields(fields)
                        .build();

        return ChatPostMessageRequest.builder()
                .username(postMessageRequest.getUsername())
                .threadTs(postMessageRequest.getThreadTs())
                .channel(postMessageRequest.getChannel())
                .attachments(List.of(attachment))
                .iconUrl(postMessageRequest.getIconUrl())
                .replyBroadcast(postMessageRequest.isReplyBroadcast())
                .build();
    }
}
