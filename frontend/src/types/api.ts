export interface ApiValidationError {
  field: string;
  message: string;
}

export interface ApiErrorResponse {
  status: number;
  error: string;
  message: string;
  path: string;
  timestamp: string;
  validationErrors: ApiValidationError[];
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface QueryFilterState {
  search: string;
  secondary?: string;
  tertiary?: string;
}

