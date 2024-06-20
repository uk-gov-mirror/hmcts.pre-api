-- Define CASE_STATE enum
CREATE TYPE public.CASE_STATE AS ENUM (
    'OPEN',
    'PENDING_CLOSURE',
    'CLOSED'
);

-- Alter cases table to include state and closed_at columns
ALTER TABLE public.cases
    ADD COLUMN state CASE_STATE NOT NULL DEFAULT 'OPEN',
    ADD COLUMN closed_at TIMESTAMPTZ;

-- Constraint to ensure closed_at is not in the future
ALTER TABLE public.cases
    ADD CONSTRAINT check_closed_at_not_in_future CHECK (closed_at <= NOW());

-- Update existing cases to default state OPEN
UPDATE public.cases SET state = 'OPEN'; 