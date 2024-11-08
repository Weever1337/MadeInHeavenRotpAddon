package com.weever.rotp_mih.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.crash.CrashReport.class)
public abstract class CrashReport {

    @Shadow
    public abstract String getExceptionMessage();

    @Redirect(method = "getFriendlyReport", at = @At(
            value = "INVOKE",
            target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            ordinal = 2)) // Reference: Ripples Of The past: mixins/TheMostUnnecessaryMixin.java
    public StringBuilder errorComment(StringBuilder crashReport, String errorComment) {
        String exceptionMessage = getExceptionMessage();
        String[] lines = exceptionMessage.split("\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.startsWith("\tat com.weever.") ||
                    line.startsWith("\tat net.minecraft.") && line.contains("$rotp_mih")) {
                return crashReport.append("it's NOT made in heaven addon...");
            }
        }
        return crashReport.append(errorComment);
    }

}
