export interface RetryOptions {
  retries?: number;
  baseDelayMs?: number;
}

function shouldRetryStatus(status: number): boolean {
  return status >= 500 && status < 600;
}

function sleep(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

function computeBackoffDelay(attempt: number, baseDelayMs: number): number {
  const jitter = Math.random() * 100; // full jitter to avoid thundering herd
  return baseDelayMs * Math.pow(2, attempt) + jitter;
}

export async function fetchWithRetry(
  input: RequestInfo | URL,
  init?: RequestInit,
  options?: RetryOptions
): Promise<Response> {
  const retries = options?.retries ?? 3;
  const baseDelayMs = options?.baseDelayMs ?? 300;

  let attempt = 0;
  let lastError: unknown;

  // attempt runs from 0..retries (inclusive of the first try, exclusive of final retry wait)
  while (attempt <= retries) {
    try {
      const response = await fetch(input as RequestInfo, init);
      if (!response.ok && shouldRetryStatus(response.status) && attempt < retries) {
        const delay = computeBackoffDelay(attempt, baseDelayMs);
        await sleep(delay);
        attempt += 1;
        continue;
      }
      return response;
    } catch (err) {
      lastError = err;
      if (attempt >= retries) {
        throw err;
      }
      const delay = computeBackoffDelay(attempt, baseDelayMs);
      await sleep(delay);
      attempt += 1;
      continue;
    }
  }

  // Should not reach here, but throw the last captured error if we do
  throw lastError instanceof Error ? lastError : new Error('fetchWithRetry failed');
}


