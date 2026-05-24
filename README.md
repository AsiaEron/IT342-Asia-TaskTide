# IT342-Asia-TaskTide
This is a productivity management system that lets users create tasks and set energy levels required for the tasks, and finish said tasks based on their energy level rather than a set timeframe. The system includes a SpringBoot backend API, React web application, and Android mobile app, all integrated to help users with their tasks across platforms.

## Gmail Email Setup
The backend can send automated email verification messages via Gmail SMTP.

### Required environment variables
Set these before running the backend:

- `MAIL_USERNAME` - your Gmail address, e.g. `youremail@gmail.com`
- `MAIL_PASSWORD` - your Gmail app password

### Recommended Gmail setup
1. Enable 2-factor authentication on your Google account.
2. Create an App Password for "Mail" and use it as `MAIL_PASSWORD`.
3. Run the backend with these environment variables set.

### Backend setup
1. Set directory to the backend folder
eg: cd tasktide/backend
2. Run the line [.\mvnw.cmd spring-boot:run]

### Frontend setup
1. Set directory to the frontend folder
eg: cd tasktide/frontend
2. Run the line [npm.cmd run dev]
