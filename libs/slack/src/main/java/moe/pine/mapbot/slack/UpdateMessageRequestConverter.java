package moe.pine.mapbot.slack;

import com.github.seratch.jslack.api.methods.request.chat.ChatUpdateRequest;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class UpdateMessageRequestConverter {
    ChatUpdateRequest convert(UpdateMessageRequest updateMessageRequest) {
        List<Attachment> attachments = new ArrayList<>();
        if (updateMessageRequest.getTextFields() != null) {
            List<Field> fields =
                    updateMessageRequest.getTextFields()
                            .stream()
                            .map(v -> Field.builder()
                                    .title(v.title())
                                    .value(v.value())
                                    .build())
                            .collect(Collectors.toUnmodifiableList());

            Attachment attachment = Attachment.builder().fields(fields).build();
            attachments.add(attachment);
        }

        return ChatUpdateRequest.builder()
                .channel(updateMessageRequest.getChannel())
                .ts(updateMessageRequest.getTs())
                .text(updateMessageRequest.getText())
                .attachments(attachments)
                .build();
    }
}
