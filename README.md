# 🚀 Hệ Thống Thực Thi Lệnh Từ Xa (Java Socket)

## 📌 Giới thiệu

Đây là hệ thống **thực thi lệnh từ xa (Remote Command Execution)** sử dụng **Java TCP Socket**, cho phép client kết nối đến server và chạy lệnh trực tiếp trên máy server.

💡 Project giúp bạn hiểu:

* Lập trình Socket TCP
* Xử lý đa luồng (Multithreading)
* Thực thi lệnh hệ điều hành
* Xây dựng ứng dụng CLI & GUI

---

## 🏗️ Kiến trúc hệ thống

```
        +------------------+
        |   Client (CLI)   |
        +------------------+
                 |
                 | TCP Socket
                 |
        +------------------+
        |     Server       |
        |  (Đa luồng)      |
        +------------------+
                 |
        +------------------+
        | Thực thi lệnh OS |
        +------------------+
```

---

## 📂 Cấu trúc project

```
📦 RemoteCommandSystem
 ┣ 📜 BaiTap15_RemoteServer.java
 ┣ 📜 BaiTap15_RemoteClient.java
 ┣ 📜 RemoteClientApp.java
 ┗ 📜 README.md
```

---

## ⚙️ Công nghệ sử dụng

* ☕ Java Core
* 🔌 TCP Socket (`Socket`, `ServerSocket`)
* 🧵 Multithreading
* 🖥️ Java Swing (GUI)
* ⚡ ProcessBuilder

---

## 🔐 Cơ chế xác thực

```
Client → Server: CONNECT
Server → Client: AUTH_REQUIRED
Client → Server: <password>
Server → Client:
    AUTH_OK  ✅
    AUTH_FAIL ❌
```

🔑 Password mặc định:

```
1103 và có thể change 
```

---

## 🔄 Giao thức truyền dữ liệu

Server trả kết quả theo format:

```
OUTPUT_BEGIN
<kết quả lệnh>
OUTPUT_END
```

✔ Giúp client xác định chính xác dữ liệu trả về

---

## 🚀 Cách chạy chương trình

### 1️⃣ Chạy Server

```bash
javac BaiTap15_RemoteServer.java
java BaiTap15_RemoteServer
```

---

### 2️⃣ Chạy Client (CLI)

```bash
javac BaiTap15_RemoteClient.java
java BaiTap15_RemoteClient
```

---

### 3️⃣ Chạy Client (GUI)

```bash
javac RemoteClientApp.java
java RemoteClientApp
```

---

## 💻 Các lệnh hỗ trợ

| Lệnh        | Mô tả                      |
| ----------- | -------------------------- |
| `pwd`       | Xem thư mục hiện tại       |
| `cd <path>` | Đổi thư mục                |
| `exit`      | Thoát                      |
| khác        | Thực thi lệnh hệ điều hành |

---

## ⚡ Tính năng nổi bật

✨ Hỗ trợ nhiều client (đa luồng)
✨ Chạy được trên Windows & Linux
✨ Có cả CLI và GUI
✨ Thực thi lệnh thời gian thực
✨ Hỗ trợ điều hướng thư mục
✨ Giao thức truyền rõ ràng

---

## 🧠 Cách hoạt động

1. Server chạy tại port `7000`
2. Client kết nối và nhập password
3. Server xác thực
4. Client gửi lệnh
5. Server:

   * Thực thi bằng `ProcessBuilder`
   * Lấy output
   * Gửi về client
6. Client hiển thị kết quả

---

## 📸 Ví dụ

```
remote-shell> dir
```

Kết quả:

```
OUTPUT_BEGIN
file1.txt
file2.java
OUTPUT_END
```

---

## ⚠️ Lưu ý bảo mật

🚨 Hiện tại:

* Password viết cứng trong code
* Dữ liệu chưa mã hóa
* Có thể chạy mọi lệnh

🔒 Nên cải thiện:

* SSL/TLS
* Hash password (SHA-256)
* Giới hạn lệnh
* Phân quyền người dùng

---

## 🔥 Hướng phát triển thêm

* 📂 Upload / Download file
* 🌐 Client web
* 🐳 Docker hóa
* 📜 Log lịch sử lệnh
* 👥 Hệ thống user

---

## 👨‍💻 Tác giả

🎓 Sinh viên CNTT – học Socket Programming.

