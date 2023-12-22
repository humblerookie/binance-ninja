import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
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



