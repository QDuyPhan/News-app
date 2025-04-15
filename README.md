# 📰 News App

Ứng dụng đọc tin tức hiện đại sử dụng Kotlin và Jetpack Compose, tích hợp API
từ [NewsAPI.org](https://newsapi.org/) để hiển thị các bài báo mới nhất từ nhiều nguồn tin tức uy
tín.


---

## 🏗️ Kiến trúc dự án

```text
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com.example.newsapp/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/              # Classes defining data
│   │   │   │   │   ├── repository/         # Classes accessing data
│   │   │   │   │   └── ...
│   │   │   │   ├── di/                     # Dependency Injection configuration
│   │   │   │   ├── ui/
│   │   │   │   │   ├── base/               # Base classes for UI components
│   │   │   │   │   ├── adapters/           # Adapters for RecyclerView or ListView
│   │   │   │   │   ├── fragments/          # Fragments in the app
│   │   │   │   │   └── viewmodels/         # ViewModels for MVVM architecture
│   │   │   │   └── util/                   # Useful utilities
│   │   ├── res/                            # Resources (layout, drawable, values, etc.)
│   └── test/                               # Directory containing tests
└── build.gradle                            # Gradle configuration file of the app
```


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