import React from "react";

export default function TableSkeleton({ rows = 6 }) {
  return (
    <div className="overflow-hidden bg-white border border-gray-200 rounded-lg">
      <div className="p-5">
        <div className="h-6 bg-gray-200 rounded w-1/3 mb-4 animate-pulse"></div>
        <div className="space-y-3">
          {Array.from({ length: rows }).map((_, i) => (
            <div key={i} className="flex items-center gap-4 p-3 bg-white/80 rounded-lg border border-gray-100">
              <div className="w-1/3 h-4 bg-gray-200 rounded animate-pulse"></div>
              <div className="w-1/4 h-4 bg-gray-200 rounded animate-pulse"></div>
              <div className="w-1/4 h-4 bg-gray-200 rounded animate-pulse"></div>
              <div className="ml-auto w-9 h-9 bg-gray-200 rounded-full animate-pulse"></div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
