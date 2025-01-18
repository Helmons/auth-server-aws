package br.pucpr.authserver.users

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.amazonaws.services.sns.model.PublishRequest


@Service
class SmsService {
    private val log = LoggerFactory.getLogger(SmsService::class.java)
    private val snsClient = AmazonSNSClientBuilder.defaultClient()

    fun sendConfirmationCode(phone: String): String {
        val code = generateCode()
        val message = "Seu código de confirmação é: $code"

        try {
            val request = PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phone)
            snsClient.publish(request)
            log.info("SMS enviado para $phone")
        } catch (e: Exception) {
            log.error("Erro ao enviar SMS: ${e.message}")
            throw RuntimeException("Falha ao enviar SMS")
        }

        return code
    }

    private fun generateCode(): String {
        return (100000..999999).random().toString()
    }
}