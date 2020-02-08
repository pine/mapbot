package moe.pine.mapbot.slack;

import com.github.seratch.jslack.api.methods.request.chat.ChatUpdateRequest;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Field;

import java.util.List;
import java.util.stream.Collectors;

class UpdateMessageRequestConverter {
    ChatUpdateRequest convert(UpdateMessageRequest updateMessageRequest) {
        List<Field> fields =
            updateMessageRequest.getTextFields()
                .stream()
                .map(v -> Field.builder()
                    .title(v.getTitle())
                    .value(v.getValue())
                    .build())
                .collect(Collectors.toUnmodifiableList());

        Attachment attachment =
            Attachment.builder()
                .fields(fields)
                .build();

        return ChatUpdateRequest.builder()
            .channel(updateMessageRequest.getChannel())
            .ts(updateMessageRequest.getTs())
            .attachments(List.of(attachment))
            .build();
    }
}
