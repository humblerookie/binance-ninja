import SwiftUI
import ComposeApp
import Firebase

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate:AppDelegate

    @Environment(\.scenePhase)
    var scenePhase: ScenePhase

	var body: some Scene {
	let homeViewControllerComponent = InjectHomeViewControllerComponent(
            applicationComponent: appDelegate.applicationComponent
    )
		WindowGroup {
			ContentView(homeViewControllerComponent: homeViewControllerComponent)
        }
	}

}

class AppDelegate: NSObject, UIApplicationDelegate {
    lazy var applicationComponent: InjectAppComponent = InjectAppComponent(
      uiViewControllerProvider: { UIApplication.topViewController()! }
    )
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        applicationComponent.initializers
                    .compactMap { ($0 as! any Initializer) }
                    .forEach { initializer in
                        initializer.initialize()
                    }
        return true
    }
}

extension UIApplication {

    private class func keyWindowCompat() -> UIWindow? {
         return UIApplication
             .shared
             .connectedScenes
             .flatMap { ($0 as? UIWindowScene)?.windows ?? [] }
             .last { $0.isKeyWindow }
     }

    class func topViewController(
        base: UIViewController? = UIApplication.keyWindowCompat()?.rootViewController
    ) -> UIViewController? {
        if let nav = base as? UINavigationController {
            return topViewController(base: nav.visibleViewController)
        }

        if let tab = base as? UITabBarController {
            let moreNavigationController = tab.moreNavigationController

            if let top = moreNavigationController.topViewController, top.view.window != nil {
                return topViewController(base: top)
            } else if let selected = tab.selectedViewController {
                return topViewController(base: selected)
            }
        }

        if let presented = base?.presentedViewController {
            return topViewController(base: presented)
        }

        return base
    }
}
