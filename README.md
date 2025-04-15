# 📰 News App

Ứng dụng đọc tin tức hiện đại sử dụng Kotlin và Jetpack Compose, tích hợp API
từ [NewsAPI.org](https://newsapi.org/) để hiển thị các bài báo mới nhất từ nhiều nguồn tin tức uy
tín.


---

## 🏗️ Kiến trúc dự án

com.example.newsapp ├── adapter # Các adapter cho RecyclerView ├── data │ ├── local # Dữ liệu cục bộ
│ │ ├── dao # DAO cho Room │ │ ├── entity # Entity của Room database │ │ ├── datastore #
Preferences & Proto DataStore │ │ └── service # Repository cho local storage │ └── remote # Dữ liệu
từ mạng │ ├── request # Model gửi API │ ├── response # Model nhận API │ └── service # Retrofit &
Repository mạng ├── di # Dependency Injection với Hilt ├── viewmodel # Tầng ViewModel ├── listener #
Interface lắng nghe sự kiện ├── paging # Paging 3 hỗ trợ phân trang ├── ui # Giao diện người dùng
chia theo màn hình (feature-based) │ ├── account │ ├── articles │ ├── base │ ├── category │ ├──
changepassword │ ├── home │ ├── main │ ├── manage │ ├── news │ ├── postnews │ ├── profile │ ├──
saved │ ├── search │ ├── splash │ ├── summary │ ├── updateuserinfo │ ├── weather │ └── widget ├──
utils # Hàm tiện ích └── NewsApp # Application class

## 🚀 Tính năng chính

- 📲 Đọc tin tức thời gian thực
- 🔍 Tìm kiếm tin tức theo từ khóa
- 💾 Lưu tin yêu thích
- 📡 Dự báo thời tiết đơn giản
- 📚 Giao diện chia màn rõ ràng (feature-based UI)
- 🔔 Push notification (FCM)
- 🌙 Hỗ trợ Dark Mode
- ⚡ Splash screen

---

## ⚙️ Công nghệ sử dụng

| Thành phần           | Công nghệ                                                    |
|----------------------|--------------------------------------------------------------|
| Dependency Injection | Dagger Hilt                                                  |
| Networking           | Retrofit, OkHttp, Gson                                       |
| Cơ sở dữ liệu        | Room, DataStore (Preferences & Proto), Firebase Firestore    |
| UI & UX              | Material Design, Navigation Component, SDPs, CircleImageView |
| Ảnh                  | Glide, Coil, Picasso, RoundedImageView                       |
| Asynchronous         | Kotlin Coroutines                                            |
| View Binding         | ViewModel, LiveData                                          |
| Phân trang           | Paging 3                                                     |
| Xử lý JSON           | Moshi, Gson, Kotlinx Serialization                           |
| Firebase             | Auth, Firestore, Database, Remote Config, Analytics, FCM     |
| Logging              | Timber                                                       |

---

## 🔧 Cài đặt

1. Clone repo:

```bash
git clone https://github.com/QDuyPhan/News-app.git