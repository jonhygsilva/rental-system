/**
 * Extract a user-friendly error message from an API error.
 *
 * Expected backend DTO:
 *   { status: number, error: string, message: string?, details: string[] }
 *
 * If the response doesn't match that shape we return a generic fallback.
 *
 * @param {unknown} err  - The error thrown by axios (or any Error)
 * @param {string} [fallback] - Optional generic message
 * @returns {string} A message ready to display to the user
 */
export function extractApiError(err, fallback = 'Ocorreu um erro inesperado. Tente novamente mais tarde.') {
  try {
    const data = err?.response?.data;

    if (data) {
      // Backend ErrorResponse DTO
      if (data.message && typeof data.message === 'string') {
        const parts = [data.message];

        if (Array.isArray(data.details) && data.details.length > 0) {
          parts.push(data.details.join('; '));
        }

        return parts.join('\n');
      }

      // Plain string body
      if (typeof data === 'string' && data.length > 0) {
        return data;
      }
    }

    // Network / timeout errors without a response
    if (err?.message) {
      return fallback;
    }
  } catch {
    // Swallow — fall through to generic
  }

  return fallback;
}
