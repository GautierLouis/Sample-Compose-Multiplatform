import UIKit
import SwiftUI
import ComposeApp
import Firebase

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



