package dev.sbs.api.io.gif;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * What happens after the frame is displayed.
 */
@Getter
@RequiredArgsConstructor
public enum DisposalMethod {

    NONE("none"),
    DO_NOT_DISPOSE("doNotDispose"),
    RESTORE_TO_BACKGROUND_COLOR("restoreToBackgroundColor"),
    RESTORE_TO_PREVIOUS_COLOR("restoreToPreviousColor");

    private final @NotNull String value;

}