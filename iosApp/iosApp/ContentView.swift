import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {

 let homeViewControllerComponent: InjectHomeViewControllerComponent
    
    init(homeViewControllerComponent: InjectHomeViewControllerComponent) {
          self.homeViewControllerComponent = homeViewControllerComponent

  	}
    func makeUIViewController(context: Context) -> UIViewController {
        homeViewControllerComponent.homeViewControllerFactory()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    let homeViewControllerComponent: InjectHomeViewControllerComponent

     init(homeViewControllerComponent: InjectHomeViewControllerComponent) {
            self.homeViewControllerComponent = homeViewControllerComponent

     }
    var body: some View {
        ComposeView(homeViewControllerComponent: homeViewControllerComponent)
                .ignoresSafeArea(.keyboard)
                .onAppear{
                    let appearance = UINavigationBarAppearance()
                                    appearance.configureWithTransparentBackground()
                    appearance.backgroundColor = AppConstants.statusColor // Change this to your desired color
                                    
                                    // Set the status bar style
                                    let viewController = UIApplication.shared.windows.first?.rootViewController
                                    viewController?.view.backgroundColor =  AppConstants.statusColor // Background color to avoid white flash
                                    viewController?.navigationController?.navigationBar.standardAppearance = appearance
                                    viewController?.navigationController?.navigationBar.scrollEdgeAppearance = appearance
                                    viewController?.setNeedsStatusBarAppearanceUpdate()
                }
    }
    
}



