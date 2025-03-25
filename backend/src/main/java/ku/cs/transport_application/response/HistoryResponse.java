package ku.cs.transport_application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class HistoryResponse {
    private String name;    // ชื่อร้านค้า
    private String address; // ที่อยู่ร้านค้า
    private String phone;   // เบอร์ร้านค้า
    private double amount;  // ราคาสินค้าทั้งหมด
}
