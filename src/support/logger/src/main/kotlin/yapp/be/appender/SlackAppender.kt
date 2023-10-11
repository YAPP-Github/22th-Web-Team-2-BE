package yapp.be.appender

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.LayoutBase
import ch.qos.logback.core.UnsynchronizedAppenderBase
import yapp.be.client.SlackLogDeliveryClient

class SlackAppender : UnsynchronizedAppenderBase<ILoggingEvent>() {

    lateinit var token: String
    lateinit var botName: String
    lateinit var botIcon: String
    lateinit var channelId: String

    private val client = SlackLogDeliveryClient()
    private val layout = object : LayoutBase<ILoggingEvent>() {
        override fun doLayout(event: ILoggingEvent): String {
            return """
                ${event.loggerName}:
                ${event.formattedMessage.replace("\n","\n\t")}
            """.trimIndent()
        }
    }
    override fun append(eventObject: ILoggingEvent) {
        client.send(
            token = token,
            botIcon = botIcon,
            botName = botName,
            channel = channelId,
            text = layout.doLayout(eventObject)
        ).subscribe()
    }
}
