# ✈️ **AirFlyerNET – Airline Ticket Reservation System**
> *This application is my project made in the process of learning Java and Angular*.  
> *It is a fork of my previous project FlightBooking-App*.  
> *I replaced the ASP.NET backend with Spring Boot and added unit tests along with admin statistics.*  
# 📌Overview

AirFlyer is a web-based airline ticket reservation system designed to provide users with a seamless booking experience. The application consists of a robust backend built with Spring Boot, a dynamic frontend developed in Angular 18.2.11, and a PostgreSQL 17.0 database for data storage.

# 🚀Key Features
✔️ **User Authentication** – Secure login and registration with role-based access.  
✔️ **JWT-Based Security** – Ensures safe communication between frontend and backend.  
✔️ **Flight Search & Booking** – Users can search, filter, and reserve flights.  
✔️ **Seat Selection** – Interactive seat selection during booking.  
✔️ **Reservation Management** – View and cancel existing reservations.  
✔️ **Admin Dashboard** – Manage flights and airports.  
✔️ **Admin Statistics** – Insights into flight trends, revenues, and user activity.  
✔️ **Pagination & Sorting** – Efficient data handling for improved performance.  
✔️ **Unit Testing** – Ensuring application reliability with service and controller tests.  

# 🖥️Backend Architecture

The backend follows the **Clean Architecture** pattern and is structured into the following layers:

📂 **Domain** – Contains entity definitions.  
📂 **Application** – Includes DTOs, interfaces, mappers, and helper functions.  
📂 **Infrastructure** – Manages services.  
📂 **Presentation** – Manages controllers.  
📂 **Shared** – Contains configs and additional classes.  

### **🔧 Core Services**

1️⃣ **AuthService** – Handles user authentication, registration, role management, and JWT-based security.  
2️⃣ **AirportService** – Manages airport creation, deletion, retrieval, and search operations.  
3️⃣ **FlightService** – Responsible for flight creation, deletion, retrieval, and search.  
4️⃣ **ReservationService** – Manages flight reservations, including booking, cancellation, and retrieval.

# 🎨Frontend Structure

The frontend, developed in **Angular 18.2.11**, is designed for an intuitive and smooth user experience. It consists of:

- **📄 Seven main pages** forming the core user journey.
- **🗂️ Five modals** used for additional interactions.
- **🛠️ Four partial components** ensuring code reusability and modularity.

 ## 📄Main Pages
 
1. **Home Page** – A flight search interface with travel tips for cost-effective booking.
2. **Flight Results Page** – Displays available flights based on user criteria.
3. **Flight Details Page** – Shows flight details, allows seat selection, and enables booking.
4. **Reservation Confirmation Page** – Displays booking details after a successful reservation.
5. **Airports Page** – Lists all available airports within the application.
6. **My Reservations Page** – Allows users to view and manage their reservations.
7. **Management Panel (Admin Only)** – Provides administrators with tools to manage airports, flights and view statistics.

## 💬Modals

1. **Login** - Allows users to authenticate and access their accounts.
2. **Register** - Enables new users to sign up and create an account.
3. **Cancel Reservation** - Confirms reservation cancellation before finalizing the action.
4. **Add airport (Admin Only)** - Provides a form for administrators to add new airports.
5. **Add flight (Admin Only)** - Allows administrators to create and manage new flights.

## 🛠️Partial components

1. **Flight Search Component** - Allows users to enter search criteria and find available flights.
2. **Navbar Component** - Provides easy navigation between pages and adapts based on user roles.
3. **Spinner Component** - Displays a loading animation when data is being fetched.
4. **Tips Component** - Offers travel advice on booking affordable flights.

# 🖼️Screenshots
Apart from Statistics the UI remains unchanged, screenshots can be seen in my previous project at the link:
https://github.com/dkbanas/FlightBooking-App

# 🔮Future Enhancements
✅ **Integration with PayPal** – Seamless payment processing.  
✅ **User Page** – Update of personal information.  
✅ **Integration Testing** – Improving overall system stability.  

# 🛠️How to run

1. Clone repo.
2. Create "airflyer_db" database in pgadmin4 and import sql file from project.
3. Configure application.properties.
4. Click run in your IDE to run backend.
5. In Client folder, open terminal and write  "ng serve" to run frontend.
