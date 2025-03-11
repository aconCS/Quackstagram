```mermaid
classDiagram

Main --> "1" SignInUI : creates
namespace App{
class Main { +main(String[] args)$ }
}

namespace Controller{
class AuthController
class NavigationController
class PostController
class UserController
}

namespace Model{
class Post
class User
}

namespace Repository{
class PostRepository
class UserRepository
}

namespace Services{
class AuthServices
class FileServices
class PostServices
class UserServices
}

namespace View{
%% authenticationUI
class AuthUIBuilder
class ComponentFactory
class SignInUI
class SignUpUI

%% components
class CommentButton
class CommentPanel
class HeaderPanel
class ImageGrid
class LikeButton
class NavigationPanel
class ProfileBody
class ProfileHeader
class UIBase
class UserNavPanel

%% coreUI
class EditProfileUI
class ExploreUI
class HomeUI
class ImageUploadUI
class NotificationsUI
class PostUI
class ProfileUI
class SearchUserUI
}

SignInUI --|> UIBase : extends
SignInUI --> NavigationController : uses
SignInUI --> "1" AuthUIBuilder : uses
SignInUI ..> HeaderPanel

class SignInUI{
-AuthController final authController;
-JTextField final usernameField;
-JTextField final passwordField;
+SignInUI()
}

SignUpUI --|> UIBase : extends
SignUpUI --> NavigationController : uses
SignUpUI --> "1" AuthUIBuilder : uses
SignUpUI ..> HeaderPanel

class SignUpUI{
-AuthController final authController;
-JTextField final usernameField;
-JTextField final passwordField;
-JTextField final bioField;
+SignUpUI()
}

AuthUIBuilder --> ComponentFactory : uses
AuthUIBuilder --> HeaderPanel : uses

class AuthUIBuilder{
-int WIDTH = 300$
-int HEIGHT = 500$
-JFrame FRAME
-ArrayList<JButton> BUTTONS
-ArrayList<JPanel> FIELD_PANELS
+AuthUIBuilder(JFrame frame, String title)
+addTextFieldPanel(String label, JTextField textField) AuthUIBuilder
+addButton(String label, Color color, ActionListener action) AuthUIBuilder
+buildUI()
}

class ComponentFactory{
~createHeader(int WIDTH) JPanel$
~createButton(String text, Color color) JButton$
~createFieldPanel(String label, JTextField textField)$
~createLogoPanel() JPanel$
}

HomeUI --|> UIBase
HomeUI --> "1" UserController : uses
HomeUI *-- "1" HeaderPanel : creates
HomeUI *-- "1" NavigationPanel : creates
HomeUI --> "1" PostController: creates
HomeUI *-- "0..*" LikeButton : creates
HomeUI --> FileServices : uses
HomeUI --> NavigationController : uses

class HomeUI{
-int final width = this.getWidth()
-int final imageWidth = width - 100
-int final imageHeight = imageWidth
-JPanel final homePanel
-UserController final userController
+HomeUI()
-buildUI()
-populateContentPanel(JPanel panel, String[][] sampleData)
-createSampleData() String[][]
}

PostUI --|> UIBase : extends
PostUI --> "1" PostController : uses
PostUI *-- "1" CommentPanel : creates
PostUI *-- "1" HeaderPanel : creates
PostUI *-- "1" NavigationPanel : creates
PostUI --> FileServices : uses
PostUI *-- "1" LikeButton : creates
PostUI *-- "1" CommentButton : creates
PostUI *-- "1" UserNavPanel : creates

class PostUI{
-int final width = this.getWidth()
-int final imageDimension = width/2
-PostController final postController
-CommentPanel final commentPanel
+PostUI(PostController postController)
-buildPostUI()
-createBodyPanel() JPanel
-createImageLabel() JLabel
-createButtonsPanel() JPanel
-createInfoPanel() JPanel
}

ProfileUI --|> UIBase : extends
ProfileUI --> "1" UserController : uses
ProfileUI *-- "1" ProfileHeader : creates
ProfileUI *-- "1" NavigationPanel : creates
ProfileUI *-- "1" ImageGrid : creates

class ProfileUI{
-int final width = this.getWidth()
-int final imageSize = width / 3
-UserController final userController
-String final username
+ProfileUI(String username)
-buildUI()
}

EditProfileUI --|> UIBase : extends
EditProfileUI --> "1" UserController: uses
EditProfileUI --> "1" HeaderPanel : creates
EditProfileUI --> "1" NavigationPanel : creates
EditProfileUI --> NavigationController : uses

class EditProfileUI{
-int PROFILE_IMAGE_SIZE = 200
-UserController final userController
+EditProfileUI(UserController userController)
-buildUI()
+createBodyPanel() JPanel
+createEditPicturePanel() JPanel
+createBioPanel() JPanel
}

NotificationsUI --|> UIBase
NotificationsUI --> UserController: uses
NotificationsUI --> "1" HeaderPanel : creates
NotificationsUI --> "1" NavigationPanel : creates

class NotificationsUI{
-UserController userController
+NotificationsUI()
-build()
```
