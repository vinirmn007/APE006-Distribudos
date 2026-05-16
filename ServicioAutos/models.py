from sqlalchemy import Column, Integer, String
from database import Base

class Auto(Base):
    __tablename__ = "autos"

    id = Column(Integer, primary_key=True, index=True)
    placa = Column(String(20), unique=True, index=True)
    modelo = Column(String(100))
    color = Column(String(50))
    anio = Column(Integer)
