import axios from "axios";
import { apiConfig } from "@/services/api/config";
import type { ApiErrorResponse } from "@/types/api";

export const apiClient = axios.create({
  baseURL: apiConfig.baseURL,
  headers: {
    "Content-Type": "application/json",
  },
});

export class ApiClientError extends Error {
  status?: number;
  details?: ApiErrorResponse;

  constructor(message: string, status?: number, details?: ApiErrorResponse) {
    super(message);
    this.name = "ApiClientError";
    this.status = status;
    this.details = details;
  }
}

export function normalizeApiError(error: unknown) {
  if (axios.isAxiosError<ApiErrorResponse>(error)) {
    const message =
      error.response?.data?.message ??
      error.response?.data?.error ??
      error.message ??
      "Request failed";

    return new ApiClientError(
      message,
      error.response?.status,
      error.response?.data,
    );
  }

  if (error instanceof Error) {
    return new ApiClientError(error.message);
  }

  return new ApiClientError("Unexpected request error");
}

export async function withMockFallback<T>(
  request: () => Promise<T>,
  fallback: () => T | Promise<T>,
) {
  try {
    return await request();
  } catch (error) {
    if (apiConfig.useMockFallback) {
      return fallback();
    }

    throw normalizeApiError(error);
  }
}

