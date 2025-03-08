```mermaid
sequenceDiagram
    actor User
    participant SignUpUI
    participant SignInUI
    participant InstagramProfileUI
    participant User

    User ->> SignUpUI: Open SignUpUI
    SignUpUI ->> User: Display registration form
    User ->> SignUpUI: Enter username, password, bio
    SignUpUI ->> User: Save user information
    SignUpUI ->> SignInUI: Navigate to SignInUI
    SignInUI ->> User: Display login form
    User ->> SignInUI: Enter username, password
    SignInUI ->> User: Verify credentials
    alt Credentials valid
        SignInUI ->> InstagramProfileUI: Open InstagramProfileUI
        InstagramProfileUI ->> User: Display user profile
    else Credentials invalid
        SignInUI ->> User: Display error message
    end
```
