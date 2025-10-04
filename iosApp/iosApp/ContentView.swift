import UIKit
import SwiftUI
import ComposeApp
import Firebase

class AppDelegate: UIResponder, UIApplicationDelegate {
    let pushManager = PushNotificationManager()

    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
        let dict = userInfo as NSDictionary
        let title = dict["title"] as? String
        let body = dict["body"] as? String
        let map = dict["data"] as? [String: String] ?? [:]
        let pushNotification = PushNotificationData(title: title, body: body, data: map)
        pushManager.sendNotification(data: pushNotification)
    }
}


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    init() {
        FirebaseApp.configure()
    }
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}



