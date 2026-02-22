import TopNavbar from "./TopNavbar";
import SideNavbar from "./SideNavbar";

export default function AppLayout({ children }) {
  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      <TopNavbar />

      <div className="flex flex-1 pt-14">
        <SideNavbar />

        <main className="flex-1 p-6 md:p-8 overflow-y-auto">
          <div className="max-w-6xl mx-auto w-full">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
}
