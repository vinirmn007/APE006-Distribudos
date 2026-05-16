from pydantic import BaseModel
from typing import Optional

class AutoBase(BaseModel):
    placa: str
    modelo: str
    color: str
    anio: int

class AutoCreate(AutoBase):
    pass

class AutoResponse(AutoBase):
    id: int

    class Config:
        from_attributes = True # Compatibilidad con Pydantic v2
