export default function Card({ title, children, footer }) {
  return (
    <div className="bg-white border border-gray-200 rounded p-5">
      {title && (
        <div className="mb-4">
          <h2 className="text-sm font-medium text-gray-900 uppercase tracking-wide">{title}</h2>
        </div>
      )}
      {children}
      {footer && <div className="mt-6 flex justify-end gap-2">{footer}</div>}
    </div>
  );
}
