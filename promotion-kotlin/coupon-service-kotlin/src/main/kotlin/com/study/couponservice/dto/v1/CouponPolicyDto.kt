import com.study.couponservice.domain.CouponPolicy
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

object CouponPolicyDto {

    data class CreateRequest(
        @field:NotBlank(message = "쿠폰 정책 이름은 필수입니다.")
        val name: String,

        val description: String? = null,

        @field:NotNull(message = "할인 타입은 필수입니다.")
        val discountType: CouponPolicy.DiscountType,

        @field:NotNull(message = "할인 값은 필수입니다.")
        @field:Min(value = 1, message = "할인 값은 1 이상이어야 합니다.")
        val discountValue: Int,

        @field:NotNull(message = "최소 주문 금액은 필수입니다.")
        @field:Min(value = 0, message = "최소 주문 금액은 0 이상이어야 합니다.")
        val minimumOrderAmount: Int,

        @field:NotNull(message = "최대 할인 금액은 필수입니다.")
        @field:Min(value = 1, message = "최대 할인 금액은 1 이상이어야 합니다.")
        val maximumDiscountAmount: Int,

        @field:NotNull(message = "총 수량은 필수입니다.")
        @field:Min(value = 1, message = "총 수량은 1 이상이어야 합니다.")
        val totalQuantity: Int,

        @field:NotNull(message = "시작 시간은 필수입니다.")
        val startTime: LocalDateTime,

        @field:NotNull(message = "종료 시간은 필수입니다.")
        val endTime: LocalDateTime
    ) {
        fun toEntity(): CouponPolicy {
            return CouponPolicy(
                name = name,
                description = description,
                discountType = discountType,
                discountValue = discountValue,
                minimumOrderAmount = minimumOrderAmount,
                maximumDiscountAmount = maximumDiscountAmount,
                totalQuantity = totalQuantity,
                startTime = startTime,
                endTime = endTime
            )
        }
    }

    data class Response(
        val id: Long,
        val name: String,
        val description: String?,
        val discountType: CouponPolicy.DiscountType,
        val discountValue: Int,
        val minimumOrderAmount: Int,
        val maximumDiscountAmount: Int,
        val totalQuantity: Int,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        companion object {
            fun from(couponPolicy: CouponPolicy): Response {
                return Response(
                    id = couponPolicy.id!!,
                    name = couponPolicy.name,
                    description = couponPolicy.description,
                    discountType = couponPolicy.discountType,
                    discountValue = couponPolicy.discountValue,
                    minimumOrderAmount = couponPolicy.minimumOrderAmount,
                    maximumDiscountAmount = couponPolicy.maximumDiscountAmount,
                    totalQuantity = couponPolicy.totalQuantity,
                    startTime = couponPolicy.startTime,
                    endTime = couponPolicy.endTime,
                    createdAt = couponPolicy.createdAt,
                    updatedAt = couponPolicy.updatedAt
                )
            }
        }
    }
}
